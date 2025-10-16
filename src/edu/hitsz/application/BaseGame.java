package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.dao.*;
import edu.hitsz.enemy.*;
import edu.hitsz.prop.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public class BaseGame extends JPanel {

    private int backGroundTop = 0;

    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 40;

    private final HeroAircraft heroAircraft;
    private final List<EnemyAircraft> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    private final List<AbstractProp> props;
    private EnemyFactory enemyFactory;
    private EnemyAircraft enemyAircraft;
    protected BufferedImage backgroundImage;

    /**
     * 屏幕中出现的敌机最大数量
     */
    private int enemyMaxNumber = 5;

    /**
     * 当前得分
     */
    private int score = 0;
    /**
     * 当前时刻
     */
    private int time = 0;

    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    private int cycleDuration = 600;
    private int cycleTime = 0;
    private int cycleTimeEnemyShoot = 0;

    /**
     * 游戏结束标志
     */
    private boolean gameOverFlag = false;

    /**
     * Boss出现分数阈值和Boss存活标志
     */
    private int bossAppearScoreThreshold = 300;
    private int scoreBuff = 0;
    private boolean bossActive = false;

    /**
     * 得分数据访问对象
     */
    private ScoreDao scoreDao;

    public BaseGame() {
        heroAircraft = HeroAircraft.getHeroAircraft();

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();


        // 初始化得分DAO
        scoreDao = new ScoreDaoImpl();

        /**
         * Scheduled 线程池，用于定时任务调度
         */
        this.executorService = new ScheduledThreadPoolExecutor(1);

        // 启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {
        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {
            time += timeInterval;

            // Boss生成逻辑（只用bossActive）
            if (!bossActive && scoreBuff >= bossAppearScoreThreshold) {
                enemyFactory = new BossFactory();
                enemyAircraft = enemyFactory.createEnemy();
                enemyAircrafts.add(enemyAircraft);
                bossActive = true;
            }

            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                System.out.println(time);
                // 新敌机产生
                if (enemyAircrafts.size() < enemyMaxNumber) {
                    // 随机生成普通敌机或精英敌机
                    if (Math.random() < 0.6) { // 80% 概率生成普通敌机
                        enemyFactory = new MobFactory();
                        enemyAircraft = enemyFactory.createEnemy();
                        enemyAircrafts.add(enemyAircraft);
                    } else if (Math.random() > 0.6 && Math.random() < 0.8) { // 20% 概率生成精英敌机
                        enemyFactory = new EliteFactory();
                        enemyAircraft = enemyFactory.createEnemy();
                        enemyAircrafts.add(enemyAircraft);
                    } else {
                        enemyFactory = new SuperEliteEnemyFactory();
                        enemyAircraft = enemyFactory.createEnemy();
                        enemyAircrafts.add(enemyAircraft);
                    }
                }
                // 英雄机射出子弹
                heroShootAction();
            }

            if (enemyCountAndNewCycleJudge()) {
                enemyShootAction();
            }

            // 子弹移动
            bulletsMoveAction();

            // 飞机移动
            aircraftsMoveAction();

            // 道具移动
            propsMoveAction();

            // 撞击检测
            crashCheckAction();

            // 后处理
            postProcessAction();

            // 每个时刻重绘界面
            repaint();

            // 游戏结束检查英雄机是否存活
            if (heroAircraft.getHp() <= 0) {
                // 游戏结束
                executorService.shutdown();
                gameOverFlag = true;
                System.out.println("Game Over!");

                // 记录得分并显示排行榜
                recordScoreAndShowLeaderboard();
            }

        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);

    }

    // ***********************
    // Action 各部分
    // ***********************

    private boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private boolean enemyCountAndNewCycleJudge() {
        cycleTimeEnemyShoot += timeInterval;
        if (cycleTimeEnemyShoot >= cycleDuration * 2) {
            // 跨越到新的周期
            cycleTimeEnemyShoot %= cycleDuration * 2;
            return true;
        } else {
            return false;
        }
    }

    private void heroShootAction() {
        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());
    }

    private void enemyShootAction() {
        // TODO 敌机射击
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyBullets.addAll(enemyAircraft.shoot());
        }
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void propsMoveAction() {
        for (AbstractProp prop : props) {
            prop.forward();
        }
    }

    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        // TODO 敌机子弹攻击英雄
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.notValid()) {
                // 英雄机已毁，不再检测
                break;
            }
            if (heroAircraft.crash(bullet)) {
                // 英雄机撞击到敌机子弹
                // 英雄机损失一定生命值
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (EnemyAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        // 获得分数，产生道具补给
                        enemyAircraft.generateProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY(), props);
                        score += enemyAircraft.getScore();
                        scoreBuff += enemyAircraft.getScore();
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // 我方获得道具，道具生效
        for (AbstractProp prop : props) {
            if (prop.notValid()) {
                continue;
            }
            if (heroAircraft.crash(prop)) {
                // 英雄机获得道具
                prop.vanish();
                prop.activate(heroAircraft);
            }
        }

    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);
        // 检查Boss是否死亡（只用bossActive）
        if (bossActive) {
            boolean bossStillAlive = false;
            for (EnemyAircraft ea : enemyAircrafts) {
                if (ea instanceof Boss) {
                    bossStillAlive = true;
                    break;
                }
            }
            if (!bossStillAlive) {
                bossActive = false;
                scoreBuff = 0;
            }
        }
    }

    // ***********************
    // Paint 各部分
    // ***********************

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景,图片滚动
        g.drawImage(backgroundImage, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(backgroundImage, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);

        paintImageWithPositionRevised(g, enemyAircrafts);

        paintImageWithPositionRevised(g, props);

        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        // 绘制得分和生命值
        paintScoreAndLife(g);

    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(new Color(16711680));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
    }

    /**
     * 记录得分并显示排行榜
     */
    private void recordScoreAndShowLeaderboard() {
        // 确定得分文件名和难度
        String scoreFileName;
        String difficulty;
        
        if (this instanceof SimpleGame) {
            scoreFileName = "scores_simple.txt";
            difficulty = "简单";
        } else if (this instanceof MiddleGame) {
            scoreFileName = "scores_middle.txt";
            difficulty = "中等";
        } else if (this instanceof HardGame) {
            scoreFileName = "scores_hard.txt";
            difficulty = "困难";
        } else {
            scoreFileName = "scores.txt";
            difficulty = "未知";
        }
        
        // 创建并显示游戏结束界面
        GameOverScreen gameOverScreen = new GameOverScreen(score, difficulty, scoreFileName);
        Main.cardPanel.add(gameOverScreen.getMainPanel(), "gameOver");
        Main.cardLayout.show(Main.cardPanel, "gameOver");
    }

}

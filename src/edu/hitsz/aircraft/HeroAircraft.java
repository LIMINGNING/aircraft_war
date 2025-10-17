package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.strategy.DirectShoot;
import edu.hitsz.strategy.ShootStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * 英雄飞机，游戏玩家操控
 * 
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {

    /** 攻击方式 */

    /**
     * 子弹一次发射数量
     */
    private int shootNum = 1;

    /**
     * 子弹伤害
     */
    private int power = 30;

    /**
     * 子弹射击方向 (向上发射：-1，向下发射：1)
     */
    private int direction = -1;

    /**
     * 双重检查锁定DCL
     */
    private volatile static HeroAircraft heroAircraft;

    /**
     * 当前火力效果线程
     */
    private volatile Thread fireEffectThread;

    /**
     * @param locationX 英雄机位置x坐标
     * @param locationY 英雄机位置y坐标
     * @param speedX    英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param speedY    英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param hp        初始生命值
     */
    private HeroAircraft(ShootStrategy shootStrategy, int locationX, int locationY, int speedX, int speedY, int hp) {
        super(shootStrategy, locationX, locationY, speedX, speedY, hp);
    }

    public static HeroAircraft getHeroAircraft() {
        if (heroAircraft == null) {
            synchronized (HeroAircraft.class) {
                if (heroAircraft == null) {
                    heroAircraft = new HeroAircraft(new DirectShoot(), Main.WINDOW_WIDTH / 2,
                            Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                            0, 0, 100);
                }
            }
        }
        return heroAircraft;
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    @Override
    /**
     * 通过射击产生子弹
     * 
     * @return 射击出的子弹List
     */
    public List<BaseBullet> shoot() {
        return heroAircraft.executeShoot(direction, shootNum, power);
    }

    public void setShootNum(int shootNum) {
        this.shootNum = shootNum;
    }

    /**
     * 设置当前火力效果线程
     * 
     * @param t 火力效果线程
     */
    public synchronized void setFireEffectThread(Thread t) {
        this.fireEffectThread = t;
    }

    /**
     * 获取当前火力效果线程
     * 
     * @return 当前火力效果线程
     */
    public synchronized Thread getFireEffectThread() {
        return fireEffectThread;
    }

}

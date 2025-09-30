package edu.hitsz.enemy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.prop.*;

import java.security.PrivateKey;
import java.util.LinkedList;
import java.util.List;

public class EliteEnemy extends EnemyAircraft {
    private int shootNum = 1;
    private int power = 30;
    private int direction = 1;

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void generateProp(int x, int y, List<AbstractProp> props) {
        double random = Math.random();
        if (random < 0.8) { // 80% 概率生成道具
            double propType = Math.random();
            PropFactory propFactory;
            AbstractProp abstractProp;
            if (propType < 0.4) {
                // 40% 概率生成加血道具
                propFactory = new BloodFactory();
                abstractProp = propFactory.createProp(x, y);
            } else if (propType < 0.7) {
                // 30% 概率生成火力道具
                propFactory = new FireFactory();
                abstractProp = propFactory.createProp(x, y);
            } else {
                // 30% 概率生成炸弹道具
                propFactory = new BombFactory();
                abstractProp = propFactory.createProp(x, y);
            }
            props.add(abstractProp);
        }
    }

    @Override
    public int getScore() {
        return 20;
    }

    @Override
    public void forward() {
        super.forward();

        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    @Override
    /**
     * 通过射击产生子弹
     * @return 射击出的子弹List
     */
    public List<BaseBullet> shoot() {
        List<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + direction*2;
        int speedX = 0;
        int speedY = this.getSpeedY() + direction*5;
        BaseBullet bullet;
        for(int i=0; i<shootNum; i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            bullet = new EnemyBullet(x + (i*2 - shootNum + 1)*10, y, speedX, speedY, power);
            res.add(bullet);
        }
        return res;
    }
}

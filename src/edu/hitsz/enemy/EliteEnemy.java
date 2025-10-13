package edu.hitsz.enemy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.prop.*;
import edu.hitsz.strategy.ShootStrategy;

import java.security.PrivateKey;
import java.util.LinkedList;
import java.util.List;

public class EliteEnemy extends EnemyAircraft {
    private int power = 10;
    private int direction = 1;

    public EliteEnemy(ShootStrategy shootStrategy, int locationX, int locationY, int speedX, int speedY, int hp) {
        super(shootStrategy, locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void generateProp(int x, int y, List<AbstractProp> props) {
        double random = Math.random();
        if (random < 0.8) { // 80% 概率生成道具
            double propType = Math.random();
            PropFactory propFactory;
            AbstractProp abstractProp;
            if (propType < 0.25) {
                propFactory = new BloodFactory();
                abstractProp = propFactory.createProp(x, y); // 道具纵向错开
            } else if (propType < 0.5) {
                propFactory = new FireFactory();
                abstractProp = propFactory.createProp(x, y);
            } else if (propType < 0.75) {
                propFactory = new BombFactory();
                abstractProp = propFactory.createProp(x, y);
            } else {
                propFactory = new FirePlusPropFactory();
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
        return this.executeShoot(direction, 1, power);
    }
}

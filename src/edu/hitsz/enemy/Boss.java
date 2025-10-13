package edu.hitsz.enemy;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.prop.*;
import edu.hitsz.strategy.ShootStrategy;

import java.util.LinkedList;
import java.util.List;

public class Boss extends EnemyAircraft{
    private int shootNum = 20;
    private int power = 10;

    public Boss(ShootStrategy shootStrategy, int locationX, int locationY, int speedX, int speedY, int hp) {
        super(shootStrategy, locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void forward() {
        super.forward();
    }

    @Override
    public void generateProp(int x, int y, List<AbstractProp> props) {
        int maxProps = 3;
        for (int i = 0; i < maxProps; i++) {
            if (Math.random() < 0.8) { // 每次有80%概率生成道具
                double propType = Math.random();
                PropFactory propFactory;
                AbstractProp abstractProp;
                if (propType < 0.25) {
                    propFactory = new BloodFactory();
                    abstractProp = propFactory.createProp(x, y + i * 30); // 道具纵向错开
                } else if (propType < 0.5) {
                    propFactory = new FireFactory();
                    abstractProp = propFactory.createProp(x, y + i * 30);
                } else if (propType < 0.75) {
                    propFactory = new BombFactory();
                    abstractProp = propFactory.createProp(x, y + i * 30);
                } else {
                    propFactory = new FirePlusPropFactory();
                    abstractProp = propFactory.createProp(x, y + i * 30);
                }
                props.add(abstractProp);
            }
        }
    }

    @Override
    public int getScore() {
        return 100;
    }

    @Override
    /**
     * 通过射击产生子弹（环形弹道：20颗子弹，均匀分布360度）
     * @return 射击出的子弹List
     */
    public List<BaseBullet> shoot() {
        return this.executeShoot(0,shootNum, power);
    }
}

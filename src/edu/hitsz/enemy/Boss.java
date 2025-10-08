package edu.hitsz.enemy;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.prop.*;

import java.util.LinkedList;
import java.util.List;

public class Boss extends EnemyAircraft{
    private int power = 10;

    public Boss(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
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
                if (propType < 0.4) {
                    propFactory = new BloodFactory();
                    abstractProp = propFactory.createProp(x, y + i * 30); // 道具纵向错开
                } else if (propType < 0.7) {
                    propFactory = new FireFactory();
                    abstractProp = propFactory.createProp(x, y + i * 30);
                } else {
                    propFactory = new BombFactory();
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
        List<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY();
        int bulletNum = 20;
        double speed = 5;
        for (int i = 0; i < bulletNum; i++) {
            double angle = 2 * Math.PI * i / bulletNum;
            int speedX = (int) Math.round(speed * Math.cos(angle));
            int speedY = (int) Math.round(speed * Math.sin(angle));
            BaseBullet bullet = new EnemyBullet(x, y, speedX, speedY, power);
            res.add(bullet);
        }
        return res;
    }
}

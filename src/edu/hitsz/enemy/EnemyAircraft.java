package edu.hitsz.enemy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BloodFactory;
import edu.hitsz.prop.BombFactory;
import edu.hitsz.prop.FireFactory;
import edu.hitsz.prop.*;

import java.util.List;

public abstract class EnemyAircraft extends AbstractAircraft {
    public EnemyAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    /**
     * 生成道具
     *
     * @param x 道具的x坐标
     * @param y 道具的y坐标
     */
    public abstract void generateProp(int x, int y, List<AbstractProp> props);
    public abstract int getScore();
}

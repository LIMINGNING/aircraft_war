package edu.hitsz.enemy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.strategy.ShootStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
public class MobEnemy extends EnemyAircraft {

    public MobEnemy(ShootStrategy shootStrategy, int locationX, int locationY, int speedX, int speedY, int hp) {
        super(shootStrategy, locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void generateProp(int x, int y, List<AbstractProp> props) {
    }

    @Override
    public int getScore() {
        return 10;
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
    public List<BaseBullet> shoot() {
        return this.executeShoot(0,0,0);
    }

}

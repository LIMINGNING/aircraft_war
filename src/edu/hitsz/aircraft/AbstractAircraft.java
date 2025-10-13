package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.strategy.ShootStrategy;

import java.util.List;

/**
 * 所有种类飞机的抽象父类：
 * 敌机（BOSS, ELITE, MOB），英雄飞机
 *
 * @author hitsz
 */
public abstract class AbstractAircraft extends AbstractFlyingObject {
    /**
     * 生命值
     */
    protected int maxHp;
    protected int hp;
    private ShootStrategy shootstrategy;

    public AbstractAircraft(ShootStrategy shootstrategy, int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY);
        this.hp = hp;
        this.maxHp = hp;
        this.shootstrategy = shootstrategy;
    }

    public void decreaseHp(int decrease){
        hp -= decrease;
        if(hp <= 0){
            hp=0;
            vanish();
        }
    }

    public void increaseHp(int hp) {
        if (this.hp + hp >= maxHp) {
            this.hp = maxHp;
        } else {
            this.hp += hp;
        }
    }

    public int getHp() {
        return hp;
    }

    /**
     * 飞机射击方法，可射击对象必须实现
     * @return
     *  可射击对象需实现，返回子弹
     *  非可射击对象空实现，返回null
     */
    public abstract List<BaseBullet> shoot();

    public void setShootstrategy(ShootStrategy shootstrategy) {
        this.shootstrategy = shootstrategy;
    }

    public List<BaseBullet> executeShoot(int direction, int shootNum, int power){
        return shootstrategy.shoot(this, direction, shootNum, power);
    }

}



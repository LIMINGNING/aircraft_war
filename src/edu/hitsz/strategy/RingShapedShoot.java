package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.enemy.EnemyAircraft;

import java.util.LinkedList;
import java.util.List;

public class RingShapedShoot implements ShootStrategy{
    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft, int direction, int shootNum, int power) {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY();
        double speed = 5;
        for (int i = 0; i < shootNum; i++) {
            BaseBullet bullet;
            double angle = 2 * Math.PI * i / shootNum;
            int speedX = (int) Math.round(speed * Math.cos(angle));
            int speedY = (int) Math.round(speed * Math.sin(angle));
            if (aircraft instanceof EnemyAircraft) {
                bullet = new EnemyBullet(x, y, speedX, speedY, power);
            } else {
                bullet = new HeroBullet(x, y, speedX, speedY, power);
            }
            res.add(bullet);
        }
        return res;
    }
}

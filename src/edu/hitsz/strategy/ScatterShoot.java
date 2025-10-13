package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.enemy.EnemyAircraft;

import java.util.LinkedList;
import java.util.List;

public class ScatterShoot implements ShootStrategy{
    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft, int direction, int shootNum, int power) {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + direction * 2;
        int speedX = aircraft.getSpeedX();
        int speedY = aircraft.getSpeedY() + direction * 5;
        BaseBullet bullet;
        for (int i = 0; i < shootNum; i++) {
            if (aircraft instanceof EnemyAircraft) {
                bullet = new EnemyBullet(x + (i * 2 - shootNum + 1) * 10, y, speedX + (i * 2 - shootNum + 1), speedY, power);
            } else {
                bullet = new HeroBullet(x + (i * 2 - shootNum + 1) * 10, y, speedX + (i * 2 - shootNum + 1), speedY, power);
            }
            res.add(bullet);
        }
        return res;
    }
}

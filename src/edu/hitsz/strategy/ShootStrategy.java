package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

public interface ShootStrategy {
    List<BaseBullet> shoot(AbstractAircraft aircraft, int direction, int shootNum, int power);
}

package edu.hitsz.enemy;

import edu.hitsz.aircraft.AbstractAircraft;

public interface EnemyFactory {
    public abstract EnemyAircraft createEnemy();
}

package edu.hitsz.enemy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.strategy.DirectShoot;
import edu.hitsz.strategy.NoShoot;

public class MobFactory implements EnemyFactory{
    @Override
    public EnemyAircraft createEnemy() {
        return new MobEnemy(new NoShoot(), (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                0,
                5,
                30);
    }
}

package edu.hitsz.enemy;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.strategy.ScatterShoot;

public class SuperEliteEnemyFactory implements EnemyFactory {
    @Override
    public SuperEliteEnemy createEnemy() {
        return new SuperEliteEnemy(new ScatterShoot(), (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.SUPER_ELITE_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                3,
                5,
                30);
    }
}

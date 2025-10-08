package edu.hitsz.enemy;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class SuperEliteEnemyFactory implements EnemyFactory {
    @Override
    public SuperEliteEnemy createEnemy() {
        return new SuperEliteEnemy((int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.SUPER_ELITE_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                3,
                5,
                30);
    }
}

package edu.hitsz.enemy;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class BossFactory implements EnemyFactory{
    @Override
    public Boss createEnemy() {
        return new Boss((int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.BOSS_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                3,
                0,
                120);
    }
}

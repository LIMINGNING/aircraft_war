package edu.hitsz.enemy;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.strategy.RingShapedShoot;

public class BossFactory implements EnemyFactory{
    @Override
    public Boss createEnemy() {
        return new Boss(new RingShapedShoot(), (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.BOSS_ENEMY_IMAGE.getWidth())),
                (int) (Main.WINDOW_HEIGHT * 0.12),
                3,
                0,
                5000);
    }
}

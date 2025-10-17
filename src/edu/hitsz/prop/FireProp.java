package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.ImageManager;
import edu.hitsz.strategy.ScatterShoot;
import edu.hitsz.strategy.DirectShoot;
import edu.hitsz.hero.HeroFireEffectThread;

public class FireProp extends AbstractProp {
    public FireProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
        width = ImageManager.PROP_FIRE_IMAGE.getWidth();
        height = ImageManager.PROP_FIRE_IMAGE.getHeight();
    }

    @Override
    public void activate(HeroAircraft hero) {
        System.out.println("FireProp activate");
        long durationMs = 8000L; // 8秒持续时间

        // 中断旧效果
        Thread old = hero.getFireEffectThread();
        if (old != null && old.isAlive())
            old.interrupt();

        // 新效果：散射
        Runnable apply = () -> {
            hero.setShootNum(3);
            hero.setShootstrategy(new ScatterShoot());
        };
        Runnable revert = () -> {
            hero.setShootNum(1);
            hero.setShootstrategy(new DirectShoot());
        };

        HeroFireEffectThread t = new HeroFireEffectThread("HeroFire-Scatter", durationMs, apply, revert);
        hero.setFireEffectThread(t);
        t.start();

        vanish();
    }
}
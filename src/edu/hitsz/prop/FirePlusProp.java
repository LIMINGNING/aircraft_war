package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.ImageManager;
import edu.hitsz.strategy.RingShapedShoot;
import edu.hitsz.strategy.DirectShoot;
import edu.hitsz.hero.HeroFireEffectThread;

public class FirePlusProp extends AbstractProp {
    public FirePlusProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
        width = ImageManager.PROP_FIREPLUS_IMAGE.getWidth();
        height = ImageManager.PROP_FIREPLUS_IMAGE.getHeight();
    }

    @Override
    public void activate(HeroAircraft hero) {
        long durationMs = 6000L; // 6秒持续时间

        // 中断旧效果
        Thread old = hero.getFireEffectThread();
        if (old != null && old.isAlive())
            old.interrupt();

        // 新效果：环形射击
        Runnable apply = () -> {
            hero.setShootNum(20);
            hero.setShootstrategy(new RingShapedShoot());
        };
        Runnable revert = () -> {
            hero.setShootNum(1);
            hero.setShootstrategy(new DirectShoot());
        };

        HeroFireEffectThread t = new HeroFireEffectThread("HeroFire-Ring", durationMs, apply, revert);
        hero.setFireEffectThread(t);
        t.start();

        vanish();
    }
}

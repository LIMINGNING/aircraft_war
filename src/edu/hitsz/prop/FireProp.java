package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.ImageManager;
import edu.hitsz.strategy.ScatterShoot;

public class FireProp extends AbstractProp{
    public FireProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
        width = ImageManager.PROP_FIRE_IMAGE.getWidth();
        height = ImageManager.PROP_FIRE_IMAGE.getHeight();
    }

    @Override
    public void activate(HeroAircraft hero) {
        System.out.println("FireProp activate");
        hero.setShootNum(3);
        hero.setShootstrategy(new ScatterShoot());
        vanish();
    }
}
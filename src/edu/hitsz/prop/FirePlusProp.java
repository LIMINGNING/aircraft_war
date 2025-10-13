package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.ImageManager;
import edu.hitsz.strategy.RingShapedShoot;
import edu.hitsz.strategy.ScatterShoot;

public class FirePlusProp extends AbstractProp{
    public FirePlusProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
        width = ImageManager.PROP_FIREPLUS_IMAGE.getWidth();
        height = ImageManager.PROP_FIREPLUS_IMAGE.getHeight();
    }

    @Override
    public void activate(HeroAircraft hero) {
        System.out.println("FirePlusProp activate");
        hero.setShootNum(20);
        hero.setShootstrategy(new RingShapedShoot());
        vanish();
    }
}

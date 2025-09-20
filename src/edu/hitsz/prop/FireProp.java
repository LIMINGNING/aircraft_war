package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.ImageManager;

public class FireProp extends AbstractProp{
    public FireProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
        width = ImageManager.PROP_FIRE_IMAGE.getWidth();
        height = ImageManager.PROP_FIRE_IMAGE.getHeight();
    }

    @Override
    public void activate(HeroAircraft hero) {
        System.out.println("BombProp activate");
        vanish();
    }
}
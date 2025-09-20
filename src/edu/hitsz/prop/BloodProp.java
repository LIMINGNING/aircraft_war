package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.ImageManager;

public class BloodProp extends AbstractProp{
    public BloodProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
        width = ImageManager.PROP_BLOOD_IMAGE.getWidth();
        height = ImageManager.PROP_BLOOD_IMAGE.getHeight();
    }

    @Override
    public void activate(HeroAircraft hero) {
        hero.increaseHp(30);
        vanish();
    }
}
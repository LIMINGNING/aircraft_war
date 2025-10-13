package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

public class FirePlusPropFactory implements PropFactory{
    @Override
    public AbstractProp createProp(int x, int y) {
        return new FirePlusProp(x,y,0,5);
    }
}

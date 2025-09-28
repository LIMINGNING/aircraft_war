package edu.hitsz.prop;

public class BloodFactory implements PropFactory{
    @Override
    public AbstractProp createProp(int x, int y) {
        return new BloodProp(x,y,0,5);
    }
}

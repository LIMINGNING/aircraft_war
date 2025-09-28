package edu.hitsz.prop;

public class FireFactory implements PropFactory{
    @Override
    public AbstractProp createProp(int x, int y) {
        return new FireProp(x,y,0,5);
    }
}

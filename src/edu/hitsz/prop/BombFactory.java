package edu.hitsz.prop;

public class BombFactory implements PropFactory{
    @Override
    public AbstractProp createProp(int x, int y) {
        return new BombProp(x,y,0,5);
    }
}

package com.headlth.management.entity;

/**
 * Created by abc on 2016/8/4.
 */
public class AnimotionCoordinate {
    private int x;
     private int y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public AnimotionCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

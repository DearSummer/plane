package com.billy.plane;

public class Vec2 {

    private float x,y;

    public Vec2() {
    }

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void reset(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void normalize() {
        float len = (float) Math.sqrt((x * x) + (y * y));
        x /= len;
        y /= len;
    }

    @Override
    public String toString() {
        return "x:" + x + "\n" + "y:" + y;
    }
}

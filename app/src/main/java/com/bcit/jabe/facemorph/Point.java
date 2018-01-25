package com.bcit.jabe.facemorph;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Xeltide on 15/01/2018.
 */

public class Point extends Drawable implements Clickable {

    Point() {
        this(0, 0);
    }

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(x, y, radius, paint);
    }

    @Override
    public boolean isClicked(float x, float y) {
        float xPrime = x - this.x;
        float yPrime = y - this.y;
        int d = (int)Math.sqrt((xPrime * xPrime) + (yPrime * yPrime));
        if (d <= radius) {
            return true;
        }
        return false;
    }

    public Point getPerpendicularVector() {
        return new Point(-y, x);
    }

    public float getVectorLength() {
        return (float)Math.sqrt((x * x) + (y * y));
    }

    public float getDistance() {
        return d;
    }

    public void setDistance(float distance) {
        d = distance;
    }

    public void add(Point adds) {
        this.x += adds.getX();
        this.y += adds.getY();
    }

    public void minus(Point subtracts) {
        this.x -= subtracts.getX();
        this.y -= subtracts.getY();
    }

    public void scale(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
    }

    public void scale(float scaleX, float scaleY) {
        this.x *= scaleX;
        this.y *= scaleY;
    }

    private int x;
    private int y;
    private float d;
    private int radius = 50;
}

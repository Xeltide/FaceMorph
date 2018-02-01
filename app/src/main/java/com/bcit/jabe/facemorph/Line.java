package com.bcit.jabe.facemorph;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.io.Serializable;

/**
 * Created by Xeltide on 15/01/2018.
 */

public class Line extends Drawable implements Serializable {

    Line() {
        this(0, 0, 0, 0);
    }

    Line(Point tail, Point head) {
        this.tail = tail;
        this.head = head;
    }

    Line(int tailX, int tailY, int headX, int headY) {
        this(new Point(tailX, tailY), new Point(headX, headY));
    }

    public void setTail(int x, int y) {
        this.tail.setPoint(x, y);
    }

    public void setHead(int x, int y) {
        this.head.setPoint(x, y);
    }

    public void setTail(Point tail) {
        this.tail = tail;
    }

    public void setHead(Point head) {
        this.head = head;
    }

    public Point getTail() {
        return tail;
    }

    public Point getHead() {
        return head;
    }

    public Point getVector() {
        return new Point(head.getX() - tail.getX(), head.getY() - tail.getY());
    }

    public void setDrawPoints(boolean drawPoints) {
        this.drawPoints = drawPoints;
    }

    public boolean isDrawPoints() {
        return drawPoints;
    }

    public void draw(Canvas canvas) {
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(12);
        canvas.drawLine(tail.getX(), tail.getY(), head.getX(), head.getY(), paint);
        paint.setColor(innerColour);
        paint.setStrokeWidth(3);
        canvas.drawLine(tail.getX(), tail.getY(), head.getX(), head.getY(), paint);
        if (drawPoints) {
            tail.draw(canvas);
            head.draw(canvas);
        }
    }

    public void setInnerColour(int colour) {
        innerColour = colour;
    }

    private boolean drawPoints = true;
    private Point tail;
    private Point head;
    private int innerColour = Color.WHITE;
}

package com.bcit.jabe.facemorph;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.io.Serializable;

/**
 * Created by Xeltide on 15/01/2018.
 */

public class LinePair extends Pair<Line> implements Serializable {

    public LinePair() {
        this(new Line(), new Line());
    }

    public LinePair(Line first, Line second) {
        super(first, second);

        Paint p = new Paint();
        p.setStrokeWidth(10);
        setPaint(p);
    }

    public LinePair(int x, int y) {
        this(new Line(), new Line());
        getFirst().setTail(x, y);
        getFirst().setHead(x, y);
        getSecond().setTail(x, y);
        getSecond().setHead(x, y);
    }

    public Point getTweenPointHead(float percentBetween) {
        int x = this.first.getHead().getX() + (int)(percentBetween * (this.second.getHead().getX() - this.first.getHead().getX()));
        int y = this.first.getHead().getY() + (int)(percentBetween * (this.second.getHead().getY() - this.first.getHead().getY()));
        return new Point(x, y);
    }

    public Point getTweenPointTail(float percentBetween) {
        int x = this.first.getTail().getX() + (int)(percentBetween * (this.second.getTail().getX() - this.first.getTail().getX()));
        int y = this.first.getTail().getY() + (int)(percentBetween * (this.second.getTail().getY() - this.first.getTail().getY()));
        return new Point(x, y);
    }

    public void setPaint(Paint paint) {
        this.first.setPaint(paint);
        this.first.getHead().setPaint(paint);
        this.first.getTail().setPaint(paint);

        this.second.setPaint(paint);
        this.second.getHead().setPaint(paint);
        this.second.getTail().setPaint(paint);
    }

    public LinePair clone() {
        LinePair pair = new LinePair();
        pair.first.setTail(this.first.getTail().getX(), this.first.getTail().getY());
        pair.first.setHead(this.first.getHead().getX(), this.first.getHead().getY());

        pair.second.setTail(this.second.getTail().getX(), this.second.getTail().getY());
        pair.second.setHead(this.second.getHead().getX(), this.second.getHead().getY());
        return pair;
    }

    public void drawFirst(Canvas canvas) {
        this.first.draw(canvas);
    }

    public void drawSecond(Canvas canvas) {
        this.second.draw(canvas);
    }

    public void setInnerColour(int colour) {
        first.setInnerColour(colour);
        first.getTail().setInnerColour(colour);
        first.getHead().setInnerColour(colour);
        second.setInnerColour(colour);
        second.getTail().setInnerColour(colour);
        second.getHead().setInnerColour(colour);
    }
}

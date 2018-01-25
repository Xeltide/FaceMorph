package com.bcit.jabe.facemorph;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Xeltide on 15/01/2018.
 */

public class LinePair extends Pair<Line> {

    LinePair() {
        this(new Line(), new Line());
    }

    LinePair(Line first, Line second) {
        super(first, second);

        Paint p = new Paint();
        p.setStrokeWidth(10);
        setPaint(p);
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
}

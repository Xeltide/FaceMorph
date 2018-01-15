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
        p.setStrokeWidth(8);
        setPaint(p);
    }

    public void setPaint(Paint paint) {
        this.first.setPaint(paint);
        this.first.getHead().setPaint(paint);
        this.first.getTail().setPaint(paint);

        this.second.setPaint(paint);
        this.second.getHead().setPaint(paint);
        this.second.getTail().setPaint(paint);
    }

    public void drawFirst(Canvas canvas) {
        this.first.draw(canvas);
    }

    public void drawSecond(Canvas canvas) {
        this.second.draw(canvas);
    }
}

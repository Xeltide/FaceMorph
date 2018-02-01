package com.bcit.jabe.facemorph;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.io.Serializable;

/**
 * Created by Xeltide on 15/01/2018.
 */

public abstract class Drawable {

    Drawable() {
        this.paint = new Paint();
    }

    abstract public void draw(Canvas canvas);

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public Paint getPaint() {
        return paint;
    }

    protected Paint paint;
}

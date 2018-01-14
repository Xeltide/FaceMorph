package com.bcit.jabe.facemorph;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Xeltide on 13/01/2018.
 */

public class PhotoEditView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private MainActivity activity;

    private float wPerH = 0;
    private int viewWidth;

    public PhotoEditView(Context context) {
        super(context);

        holder = getHolder();
        holder.addCallback(this);
    }

    public PhotoEditView(Context context, AttributeSet attrs) {
        super(context, attrs);

        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Bitmap b = activity.getStore().getStartFrame();
        float w = b.getWidth();
        float h = b.getHeight();
        wPerH = w / h;

        viewWidth = (int)(getHeight() * wPerH);
        int newW = getWidth() - viewWidth;
        android.view.ViewGroup.MarginLayoutParams s = (android.view.ViewGroup.MarginLayoutParams)this.getLayoutParams();
        s.setMargins(newW / 2, 0, newW / 2, 0);
        requestLayout();
        android.util.Log.d("SETUP SIZE","Wph bigger");

        Canvas c = holder.lockCanvas(null);
        draw(c);
        holder.unlockCanvasAndPost(c);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.BLACK);
        Bitmap scaled = Bitmap.createScaledBitmap(activity.getStore().getStartFrame(), viewWidth, getHeight(), true);
        canvas.drawBitmap(scaled, 0, 0, null);
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }
}

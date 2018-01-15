package com.bcit.jabe.facemorph;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Xeltide on 14/01/2018.
 */

public class PhotoEditThread extends Thread {

    private final int fps = 60;
    private final float frameTime = 1.0f / fps;

    private SurfaceHolder holder;
    private PhotoEditView view;
    private Canvas canvas;

    private boolean run = false;

    public PhotoEditThread(SurfaceHolder holder, PhotoEditView view) {
        this.holder = holder;
        this.view = view;
    }

    public void setRunning(boolean isRunning) {
        run = isRunning;
    }

    public void run() {

        long lastTime = System.currentTimeMillis();
        while(run) {

            try {
                canvas = holder.lockCanvas(null);

                synchronized(holder) {
                    view.draw(canvas);
                }
            } finally {
                if(canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }

            long dt = System.currentTimeMillis() - lastTime;
            lastTime = System.currentTimeMillis();
            if (dt < frameTime * 1000) {
                try {
                    sleep((int)((frameTime * 1000) - dt));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

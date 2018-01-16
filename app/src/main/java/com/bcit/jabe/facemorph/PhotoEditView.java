package com.bcit.jabe.facemorph;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by Xeltide on 13/01/2018.
 */

public class PhotoEditView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private MainActivity activity;
    private PhotoEditThread thread;

    private Bitmap currentImage;
    private float wPerH = 0;
    private int viewWidth;
    private int viewMaxW;
    private int viewHeight;
    private int viewMaxH;

    private Point heldPoint;

    public PhotoEditView(Context context) {
        super(context);

        holder = getHolder();
        holder.addCallback(this);
        thread = new PhotoEditThread(holder, this);
    }

    public PhotoEditView(Context context, AttributeSet attrs) {
        super(context, attrs);

        holder = getHolder();
        holder.addCallback(this);
        thread = new PhotoEditThread(holder, this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        viewMaxW = getWidth();
        viewMaxH = getHeight();

        if (currentImage != null) {
            setViewMargins(currentImage);
        }

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        thread.setRunning(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PhotoEditMode mode = activity.getStore().getPhotoEditMode();
        int action = event.getAction();

        if (mode == PhotoEditMode.DRAW) {
            drawTouch(event, action);
        } else if (mode == PhotoEditMode.MOVE) {
            moveTouch(event, action);
        } else if (mode == PhotoEditMode.ERASE) {
            eraseTouch(event, action);
        }

        return true;
    }

    private void drawTouch(MotionEvent event, int action) {
        if (action == MotionEvent.ACTION_DOWN) {
            LinePair p = new LinePair();
            p.getFirst().setTail((int)event.getX(), (int)event.getY());
            p.getFirst().setHead((int)event.getX(), (int)event.getY());
            p.getSecond().setTail((int)event.getX(), (int)event.getY());
            p.getSecond().setHead((int)event.getX(), (int)event.getY());

            activity.getStore().addLinePair(p);
        } else if (action == MotionEvent.ACTION_MOVE) {
            ArrayList<LinePair> lines = activity.getStore().getLinePairs();
            LinePair p = lines.get(lines.size() - 1);
            p.getFirst().setHead((int)event.getX(), (int)event.getY());
            p.getSecond().setHead((int)event.getX(), (int)event.getY());
        }
    }

    private void moveTouch(MotionEvent event, int action) {
        if (action == MotionEvent.ACTION_DOWN) {
            for (LinePair pair : activity.getStore().getLinePairs()) {
                Line l = pair.getFirst();
                if (l.getTail().isClicked(event.getX(), event.getY())) {
                    heldPoint = l.getTail();
                    break;
                } else if (l.getHead().isClicked(event.getX(), event.getY())) {
                    heldPoint = l.getHead();
                    break;
                }

                l = pair.getSecond();
                if (l.getTail().isClicked(event.getX(), event.getY())) {
                    heldPoint = l.getTail();
                    break;
                } else if (l.getHead().isClicked(event.getX(), event.getY())) {
                    heldPoint = l.getHead();
                    break;
                }
            }
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (heldPoint != null) {
                heldPoint.setPoint((int)event.getX(), (int)event.getY());
            }
        } else if (action == MotionEvent.ACTION_UP) {
            heldPoint = null;
        }
    }

    private void eraseTouch(MotionEvent event, int action) {
        if (action == MotionEvent.ACTION_DOWN) {
            ArrayList<LinePair> lines = activity.getStore().getLinePairs();
            for (int i = 0; i < lines.size(); i++) {
                LinePair pair = lines.get(i);
                Line l = pair.getFirst();
                if (l.getTail().isClicked(event.getX(), event.getY())) {
                    activity.getStore().removeLinePair(i);
                    break;
                } else if (l.getHead().isClicked(event.getX(), event.getY())) {
                    activity.getStore().removeLinePair(i);
                    break;
                }

                l = pair.getSecond();
                if (l.getTail().isClicked(event.getX(), event.getY())) {
                    activity.getStore().removeLinePair(i);
                    break;
                } else if (l.getHead().isClicked(event.getX(), event.getY())) {
                    activity.getStore().removeLinePair(i);
                    break;
                }
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null) {
            super.draw(canvas);
            canvas.drawColor(Color.BLACK);
            if (currentImage != null) {
                Bitmap scaled = Bitmap.createScaledBitmap(currentImage, viewWidth, viewHeight, true);
                canvas.drawBitmap(scaled, 0, 0, null);
            }

            if (activity.getStore().isFirstFrame()) {
                for (LinePair pair : activity.getStore().getLinePairs()) {
                    pair.drawFirst(canvas);
                }
            } else {
                for (LinePair pair : activity.getStore().getLinePairs()) {
                    pair.drawSecond(canvas);
                }
            }
        }
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    public void setCurrentImage(Bitmap bitmap) {
        setViewMargins(bitmap);
        this.currentImage = bitmap;
    }

    private void setViewMargins(Bitmap b) {
        android.view.ViewGroup.MarginLayoutParams s = (android.view.ViewGroup.MarginLayoutParams)this.getLayoutParams();

        if (b != null) {
            float w = b.getWidth();
            float h = b.getHeight();
            // TODO: remove wPerH
            wPerH = w / h;
            int viewW = (int)(viewMaxH * wPerH);
            if (viewW > viewMaxW) {
                viewWidth = viewMaxW;
                viewHeight = (int)(viewMaxW * (h / w));
                int newH = viewMaxH - viewHeight;
                if (newH != 0) {
                    s.setMargins(0, newH / 2, 0, newH / 2);
                    requestLayout();
                }
            } else {
                viewWidth = viewW;
                viewHeight = viewMaxH;
                int newW = viewMaxW - viewWidth;
                if (newW != 0) {
                    s.setMargins(newW / 2, 0, newW / 2, 0);
                    requestLayout();
                }
            }
        } else {
            s.setMargins(0, 0, 0, 0);
            requestLayout();
        }
    }
}

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
    private ActivityStore store;
    private PhotoEditThread thread;

    private Bitmap currentImage;
    private Bitmap scaledImage;
    private boolean loadImage = false;
    private float wPerH = 0;
    private int viewWidth;
    private int viewMaxW;
    private int viewHeight;
    private int viewMaxH;

    private Point heldPoint;
    private LinePair lastDraw;
    private LinePair lastMove;
    private LinePair eraseLine;


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
        PhotoEditMode mode = store.getPhotoEditMode();
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
            clearLastDrawPair();
            LinePair p = new LinePair((int)event.getX(), (int)event.getY());
            p.setInnerColour(Color.GREEN);
            lastDraw = p;

            store.addLinePair(p);
        } else if (action == MotionEvent.ACTION_MOVE) {
            ArrayList<LinePair> lines = store.getLinePairs();
            LinePair p = lines.get(lines.size() - 1);
            p.getFirst().setHead((int)event.getX(), (int)event.getY());
            p.getSecond().setHead((int)event.getX(), (int)event.getY());
        }
    }

    public void clearLastDrawPair() {
        if (lastDraw != null) {
            lastDraw.setInnerColour(Color.WHITE);
        }
        lastDraw = null;
    }

    public void clearLastMovePair() {
        if (lastMove != null) {
            lastMove.setInnerColour(Color.WHITE);
        }
        lastMove = null;
    }

    private void moveTouch(MotionEvent event, int action) {
        if (action == MotionEvent.ACTION_DOWN) {
            for (LinePair pair : store.getLinePairs()) {
                Line l;

                if (store.isFirstFrame()) {
                    l = pair.getFirst();
                    if (l.getTail().isClicked(event.getX(), event.getY())) {
                        setMovingPoint(l.getTail(), pair);
                        break;
                    } else if (l.getHead().isClicked(event.getX(), event.getY())) {
                        setMovingPoint(l.getHead(), pair);
                        break;
                    }
                } else {
                    l = pair.getSecond();
                    if (l.getTail().isClicked(event.getX(), event.getY())) {
                        setMovingPoint(l.getTail(), pair);
                        break;
                    } else if (l.getHead().isClicked(event.getX(), event.getY())) {
                        setMovingPoint(l.getHead(), pair);
                        break;
                    }
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

    private void setMovingPoint(Point p, LinePair pair) {
        clearLastMovePair();
        heldPoint = p;
        pair.setInnerColour(Color.BLUE);
        lastMove = pair;
    }

    private void eraseTouch(MotionEvent event, int action) {
        if (action == MotionEvent.ACTION_DOWN) {
            ArrayList<LinePair> lines = store.getLinePairs();
            // Line pair already selected
            if (eraseLine != null) {
                for (int i = 0; i < lines.size(); i++) {
                    LinePair pair = lines.get(i);
                    pair.setInnerColour(Color.WHITE);
                    Line l;

                    if (pair == eraseLine) {
                        if (store.isFirstFrame()) {
                            l = pair.getFirst();
                            if (l.getTail().isClicked(event.getX(), event.getY())) {
                                store.removeLinePair(i);
                                break;
                            } else if (l.getHead().isClicked(event.getX(), event.getY())) {
                                store.removeLinePair(i);
                                break;
                            }
                        } else {
                            l = pair.getSecond();
                            if (l.getTail().isClicked(event.getX(), event.getY())) {
                                store.removeLinePair(i);
                                break;
                            } else if (l.getHead().isClicked(event.getX(), event.getY())) {
                                store.removeLinePair(i);
                                break;
                            }
                        }
                    }
                }
                eraseLine = null;
            // Line pair not selected
            } else {
                for (int i = 0; i < lines.size(); i++) {
                    LinePair pair = lines.get(i);
                    Line l;

                    if (store.isFirstFrame()) {
                        l = pair.getFirst();
                        if (l.getTail().isClicked(event.getX(), event.getY())) {
                            setEraseLine(pair);
                            break;
                        } else if (l.getHead().isClicked(event.getX(), event.getY())) {
                            setEraseLine(pair);
                            break;
                        }
                    } else {
                        l = pair.getSecond();
                        if (l.getTail().isClicked(event.getX(), event.getY())) {
                            setEraseLine(pair);
                            break;
                        } else if (l.getHead().isClicked(event.getX(), event.getY())) {
                            setEraseLine(pair);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void setEraseLine(LinePair pair) {
        eraseLine = pair;
        eraseLine.setInnerColour(Color.RED);
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null) {
            super.draw(canvas);
            canvas.drawColor(Color.BLACK);

            if (scaledImage != null && scaledImage.getWidth() == viewWidth && scaledImage.getHeight() == viewHeight && !loadImage) {
                store.setBitmapScale(new Point(scaledImage.getWidth(), scaledImage.getHeight()));
                canvas.drawBitmap(scaledImage, 0, 0, null);
            } else if (currentImage != null){
                scaledImage = Bitmap.createScaledBitmap(currentImage, viewWidth, viewHeight, true);
                loadImage = false;
            }

            ArrayList<LinePair> pairs = store.getLinePairs();
            if (store.isFirstFrame()) {
                for (int i = 0; i < pairs.size(); i++) {
                    LinePair pair = pairs.get(i);
                    pair.drawFirst(canvas);
                }
            } else {
                for (int i = 0; i < pairs.size(); i++) {
                    LinePair pair = pairs.get(i);
                    pair.drawSecond(canvas);
                }
            }
        }
    }

    public void setStore(ActivityStore store) {
        this.store = store;
    }

    public void setCurrentImage(Bitmap bitmap) {
        setViewMargins(bitmap);
        this.currentImage = bitmap;
        this.loadImage = true;
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

    public void resetLinePairColour() {
        for (LinePair lp : store.getLinePairs()) {
            lp.setInnerColour(Color.WHITE);
        }
    }
}

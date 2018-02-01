package com.bcit.jabe.facemorph;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by Xeltide on 25/01/2018.
 */

public class MorphParentThread extends Thread {

    private Bitmap start;
    private Bitmap end;
    private ArrayList<LinePair> controlLines;
    private ArrayList<LinePair> reverseCtrlLines;
    private int frameCount;
    private Point frameScale;
    private ActivityStore store;

    private LinkedList<Bitmap> forwardMap;
    private LinkedList<Bitmap> reverseMap;

    private ProgressBar progress;
    private ImageView frameView;

    MorphParentThread(ActivityStore store, ProgressBar progress, ImageView frameView) {
        this.store = store;
        this.start = store.getStartFrame();
        this.end = store.getEndFrame();
        this.controlLines = store.getLinePairs();
        this.reverseCtrlLines = flipCtrlLines(controlLines);
        this.frameCount = store.getFrameCount();
        this.frameScale = store.getBitmapScale();
        this.progress = progress;
        this.frameView = frameView;
    }

    private ArrayList<LinePair> flipCtrlLines(ArrayList<LinePair> input) {
        ArrayList<LinePair> output = new ArrayList<>();

        for (LinePair lp : input) {
            LinePair tempPair = lp.clone();
            Line tempLine = tempPair.getFirst();
            tempPair.setFirst(tempPair.getSecond());
            tempPair.setSecond(tempLine);
            output.add(tempPair);
        }

        return output;
    }

    public void run() {
        Thread forwardProc = new Thread() {

            public void run() {
                Morpher m1 = new Morpher(start, end, controlLines, frameCount, frameScale);
                forwardMap = m1.morph();
            }
        };

        Thread reverseProc = new Thread() {

            public void run() {
                Morpher m2 = new Morpher(end, start, reverseCtrlLines, frameCount, frameScale);
                reverseMap = m2.morph();
            }
        };

        forwardProc.start();
        reverseProc.start();
        try {
            forwardProc.join();
            reverseProc.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        store.setFrames(crossDissolve());
        store.setIsMorphing(false);
        store.setIsFaceMorphed(true);

        frameView.post(new Runnable() {

            @Override
            public void run() {
                frameView.setImageBitmap(store.getStartFrame());
                store.setViewingFrameNum(-1);
            }
        });
        progress.post(new Runnable() {

            @Override
            public void run() {
                progress.setVisibility(View.GONE);
            }
        });
    }

    private LinkedList<Bitmap> crossDissolve() {
        LinkedList<Bitmap> bmaps = new LinkedList<>();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        int frame = 1;
        float morphStep = 1.0f / (frameCount + 1);
        while (forwardMap.size() > 0) {
            Bitmap bitmap = Bitmap.createBitmap(start.getWidth(), start.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bitmap);

            for (int y = 0; y < start.getHeight(); y++) {
                for (int x = 0; x < start.getWidth(); x++) {
                    float reverseW = (frame * morphStep);
                    float forwardW = 1 - reverseW;
                    int colour1 = forwardMap.get(0).getPixel(x, y);
                    int colour2 = reverseMap.get(0).getPixel(x, y);
                    float r = (Color.red(colour1) * forwardW) + (Color.red(colour2) * reverseW);
                    float g = (Color.green(colour1) * forwardW) + (Color.green(colour2) * reverseW);
                    float b = (Color.blue(colour1) * forwardW) + (Color.blue(colour2) * reverseW);

                    paint.setColor(Color.rgb((int)r, (int)g, (int)b));
                    c.drawPoint(x, y, paint);
                }
            }

            bmaps.add(bitmap);
            forwardMap.remove(0);
            reverseMap.remove(0);
            frame++;
        }

        return bmaps;
    }
}

package com.bcit.jabe.facemorph;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Xeltide on 23/01/2018.
 */

public class Morpher {

    Bitmap start;
    Bitmap end;
    ArrayList<LinePair> controlLines;
    int frameCount;
    Point frameScale;

    Morpher(Bitmap start, Bitmap end, ArrayList<LinePair> controlLines, int frameCount, Point frameScale) {
        this.start = start;
        this.end = end;
        this.controlLines = controlLines;
        this.frameCount = frameCount;
        this.frameScale = frameScale;
    }

    public LinkedList<Bitmap> morph() {
        LinkedList<Bitmap> betweenFrames = new LinkedList<>();
        LinkedList<ArrayList<Line>> frameLines = generateLines();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        for (int frame = 1; frame <= frameCount; frame++) {
            Bitmap b = Bitmap.createBitmap(start.getWidth(), start.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            for (int y = 0; y < b.getHeight(); y++) {
                for (int x = 0; x < b.getWidth(); x++) {
                    ArrayList<Point> primePoints = new ArrayList<>();
                    Point p = new Point(x, y);
                    for (int line = 0 ; line < frameLines.get(frame).size(); line++) {
                        Point pPrm = getSrcPixel(frameLines.get(frame).get(line), p, frameLines.get(frame - 1).get(line));
                        primePoints.add(pPrm);
                    }

                    Point finalSource = getWeightedPoint(p, primePoints);
                    if (finalSource.getX() < 0) {
                        finalSource.setX(0);
                    } else if (finalSource.getX() >= b.getWidth()) {
                        finalSource.setX(b.getWidth() - 1);
                    }
                    if (finalSource.getY() < 0) {
                        finalSource.setY(0);
                    } else if (finalSource.getY() >= b.getHeight()) {
                        finalSource.setY(b.getHeight() - 1);
                    }


                    Bitmap src;
                    if (frame == 1) {
                        src = start;
                    } else {
                        src = betweenFrames.getLast();
                    }
                    int pixel = src.getPixel(finalSource.getX(), finalSource.getY());
                    paint.setColor(pixel);
                    c.drawPoint(x, y, paint);
                }
            }
            betweenFrames.add(b);
        }

        int h = betweenFrames.get(0).getHeight();
        int b = betweenFrames.get(0).getWidth();
        return betweenFrames;
    }

    // Generates the tween lines for all of the new frames
    private LinkedList<ArrayList<Line>> generateLines() {
        float xScale = (float)start.getWidth() / frameScale.getX();
        float yScale = (float)start.getHeight() / frameScale.getY();
        LinkedList<ArrayList<Line>> frameLines = new LinkedList<>();
        ArrayList<Line> startLines = new ArrayList<Line>();
        ArrayList<Line> endLines = new ArrayList<Line>();

        for (int i = 0; i < frameCount; i++) {
            frameLines.add(new ArrayList<Line>());
        }

        float morphStep = 1.0f / (frameCount + 1);
        for (int lp = 0; lp < controlLines.size(); lp++) {
            LinePair cur = controlLines.get(lp).clone();
            cur.getFirst().getTail().scale(xScale, yScale);
            cur.getFirst().getHead().scale(xScale, yScale);
            cur.getSecond().getTail().scale(xScale, yScale);
            cur.getSecond().getHead().scale(xScale, yScale);

            startLines.add(cur.getFirst());
            endLines.add(cur.getSecond());
            for (int frame = 1; frame <= frameCount; frame++) {
                frameLines.get(frame - 1).add(getTweenLine(cur, frame * morphStep));
            }
        }
        frameLines.addFirst(startLines);
        frameLines.addLast(endLines);

        return frameLines;
    }

    // Generates individual lines
    private Line getTweenLine(LinePair lp, float percentOnLine) {
        Point head = lp.getTweenPointHead(percentOnLine);
        Point tail = lp.getTweenPointTail(percentOnLine);
        Line l = new Line(tail, head);

        return l;
    }

    // Returns the transformed point based on two control lines
    public Point getSrcPixel(Line start, Point pixel, Line end) {
        Point pq = start.getVector();
        Point normal = pq.getPerpendicularVector();
        Point pt = new Line(start.getTail(), pixel).getVector();
        Point tp = new Line(pixel, start.getTail()).getVector();
        float d = projectVectorLength(tp, normal);
        float fractionalLength = projectVectorLength(pt, pq) / pq.getVectorLength();

        Point pqPrm = end.getVector();
        Point normalPrm = pqPrm.getPerpendicularVector();
        int x = (int)(end.getTail().getX() + (fractionalLength * pqPrm.getX()) - (d * normalPrm.getX() / normalPrm.getVectorLength()));
        int y = (int)(end.getTail().getY() + (fractionalLength * pqPrm.getY()) - (d * normalPrm.getY() / normalPrm.getVectorLength()));

        Point vector = new Point(x, y);
        vector.setDistance(d);
        return vector;
    }

    // Projects a onto b
    // b . a
    // -----
    //  |b|
    public float projectVectorLength(Point a, Point b) {
        return ((a.getX() * b.getX()) + (a.getY() * b.getY())) / b.getVectorLength();
    }

    public Point getWeightedPoint(Point origin, ArrayList<Point> primePoints) {
        float avgX = 0;
        float avgY = 0;
        float totalWeight = 0;
        for (int i = 0; i < primePoints.size(); i++) {
            float weight = getWeight(primePoints.get(i).getDistance());
            float x = primePoints.get(i).getX();
            float y = primePoints.get(i).getY();
            x -= origin.getX();
            y -= origin.getY();
            x *= weight;
            y *=  weight;
            avgX += x;
            avgY += y;
            totalWeight += weight;
        }

        avgX /= totalWeight;
        avgY /= totalWeight;
        avgX += origin.getX();
        avgY += origin.getY();

        return new Point(Math.round(avgX), Math.round(avgY));
    }

    public float getWeight(float distance) {
        return (float)Math.pow(1 / (0.01 + distance), 2);
    }
}

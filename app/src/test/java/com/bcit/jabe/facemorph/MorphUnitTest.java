package com.bcit.jabe.facemorph;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MorphUnitTest {
    @Test
    public void LinePair_getTweenPointHead_isCorrect() throws Exception {
        LinePair lp = new LinePair();
        lp.getFirst().setHead(-15, 5);
        lp.getSecond().setHead(15,10);
        assertEquals(0, lp.getTweenPointHead(0.5f).getX());
        assertEquals(7, lp.getTweenPointHead(0.5f).getY());
    }

    @Test
    public void LinePair_getTweenPointTail_isCorrect() throws Exception {
        LinePair lp = new LinePair();
        lp.getFirst().setTail(-15, 5);
        lp.getSecond().setTail(15,10);
        assertEquals(0, lp.getTweenPointTail(0.5f).getX());
        assertEquals(7, lp.getTweenPointTail(0.5f).getY());
    }

    @Test
    public void Morph_getSrcPixel_isCorrect() throws Exception {
        Morpher m = new Morpher(null, null, null, 1);
        Line start = new Line(100, 300, 300, 100);
        Line end = new Line(100, 200, 300, 150);
        Point p = new Point(0, 0);
        Point p2 = m.getSrcPixel(start, p, end);
        assertEquals(131, p2.getX());
        assertEquals(-99, p2.getY());
    }
}
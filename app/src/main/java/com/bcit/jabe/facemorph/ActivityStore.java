package com.bcit.jabe.facemorph;

import android.graphics.Bitmap;
import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Xeltide on 12/01/2018.
 */

public class ActivityStore implements Serializable {

    private int lastItem = R.id.nav_choose_pictures;
    private PhotoEditMode editAction = PhotoEditMode.DRAW;

    private Bitmap startFrame;
    private Bitmap endFrame;
    private LinkedList<Bitmap> frames;
    private int frameCount = 0;
    private ArrayList<LinePair> lines;
    private boolean firstFrame = true;
    private boolean faceMorphed = false;
    private int viewingFrameNum = -1;
    private Point bitmapScale;

    ActivityStore() {
        this.lines = new ArrayList<>();
    }

    public Point getBitmapScale() {
        return bitmapScale;
    }

    public void setBitmapScale(Point p) {
        this.bitmapScale = p;
    }

    public void setStartFrame(Bitmap bitmap) {
        startFrame = bitmap;
    }

    public Bitmap getStartFrame() {
        return startFrame;
    }

    public void setEndFrame(Bitmap bitmap) {
        endFrame = bitmap;
    }

    public Bitmap getEndFrame() {
        return endFrame;
    }

    public void setFrames(LinkedList<Bitmap> frames) {
        this.frames = frames;
    }

    public void addFrame(Bitmap bitmap) {
        frames.add(bitmap);
    }

    public LinkedList<Bitmap> getFrames() {
        return frames;
    }

    public Bitmap getFrame(int index) {
        return frames.get(index);
    }

    public void setFrameCount(int totalFrames) {
        frameCount = totalFrames;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public void setLastItem(int id) {
        lastItem = id;
    }

    public int getLastItem() {
        return lastItem;
    }

    public void setIsFirstFrame(boolean isFirstFrame) {
        firstFrame = isFirstFrame;
    }

    public boolean isFirstFrame() {
        return firstFrame;
    }

    public void setIsFaceMorphed(boolean isFaceMorphed) {
        faceMorphed = isFaceMorphed;
    }

    public boolean isFaceMorphed() {
        return faceMorphed;
    }

    public PhotoEditMode getPhotoEditMode() {
        return editAction;
    }

    public void setPhotoEditMode(PhotoEditMode mode) {
        editAction = mode;
    }

    public ArrayList<LinePair> getLinePairs() {
        return lines;
    }

    public void addLinePair(LinePair pair) {
        lines.add(pair);
    }

    public void removeLinePair(int index) {
        lines.remove(index);
    }

    public int getViewingFrameNum() {
        return viewingFrameNum;
    }

    public void setViewingFrameNum(int index) {
        viewingFrameNum = index;
    }

    public void saveToBundle(Bundle bundle) {
        bundle.putParcelable("startFrame", startFrame);
        bundle.putParcelable("endFrame", endFrame);
        bundle.putInt("lastItem", lastItem);
        // TODO: save lines to bundle
    }

    public void loadFromBundle(Bundle bundle) {
        startFrame = bundle.getParcelable("startFrame");
        endFrame = bundle.getParcelable("endFrame");
        lastItem = bundle.getInt("lastItem");
        // TODO: load lines from bundle
    }
}

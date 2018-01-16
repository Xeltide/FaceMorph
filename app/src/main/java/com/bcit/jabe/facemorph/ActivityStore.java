package com.bcit.jabe.facemorph;

import android.graphics.Bitmap;
import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Xeltide on 12/01/2018.
 */

public class ActivityStore implements Serializable {

    private int lastItem = R.id.nav_choose_pictures;
    private PhotoEditMode editAction = PhotoEditMode.DRAW;

    private Bitmap startFrame;
    private Bitmap endFrame;
    private ArrayList<LinePair> lines;
    private boolean firstFrame = true;

    ActivityStore() {
        this.lines = new ArrayList<>();
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

    public void setLastItem(int id) {
        lastItem = id;
    }

    public int getLastItem() {
        return lastItem;
    }

    public void setFirstFrame(boolean isFirstFrame) {
        firstFrame = isFirstFrame;
    }

    public boolean isFirstFrame() {
        return firstFrame;
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

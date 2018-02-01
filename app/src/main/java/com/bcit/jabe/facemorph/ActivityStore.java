package com.bcit.jabe.facemorph;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Xeltide on 12/01/2018.
 */

public class ActivityStore implements Serializable {

    private int lastItem = R.id.nav_choose_pictures;
    private PhotoEditMode editAction = PhotoEditMode.DRAW;

    private SerializableBitmap startFrame;
    private SerializableBitmap endFrame;
    private LinkedList<SerializableBitmap> frames;
    private int frameCount = 0;
    private ArrayList<LinePair> lines;
    private boolean firstFrame = true;
    private boolean faceMorphed = false;
    private boolean isMorphing = false;
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
        startFrame = new SerializableBitmap(bitmap);
    }

    public Bitmap getStartFrame() {
        if (startFrame != null) {
            return startFrame.getBitmap();
        }

        return null;
    }

    public void setEndFrame(Bitmap bitmap) {
        endFrame = new SerializableBitmap(bitmap);
    }

    public Bitmap getEndFrame() {
        if (endFrame != null) {
            return endFrame.getBitmap();
        }

        return null;
    }

    public void setFrames(LinkedList<Bitmap> frames) {
        this.frames = new LinkedList<SerializableBitmap>();
        for (Bitmap b : frames) {
            this.frames.add(new SerializableBitmap(b));
        }
    }

    public void addFrame(Bitmap bitmap) {
        frames.add(new SerializableBitmap(bitmap));
    }

    public LinkedList<Bitmap> getFrames() {
        LinkedList<Bitmap> bitmapFrames = new LinkedList<>();
        for (SerializableBitmap b : frames) {
            bitmapFrames.add(b.getBitmap());
        }
        return bitmapFrames;
    }

    public Bitmap getFrame(int index) {
        return frames.get(index).getBitmap();
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

    public void setIsMorphing(boolean morphing) {
        isMorphing = morphing;
    }

    public boolean isMorphing() {
        return isMorphing;
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
        bundle.putSerializable("startFrame", startFrame);
        bundle.putSerializable("endFrame", endFrame);
        bundle.putInt("lastItem", lastItem);
        // TODO: save lines to bundle
    }

    public void loadFromBundle(Bundle bundle) {
        startFrame = (SerializableBitmap)bundle.getSerializable("startFrame");
        endFrame = (SerializableBitmap)bundle.getSerializable("endFrame");
        lastItem = bundle.getInt("lastItem");
        // TODO: load lines from bundle
    }

    public void save(String fileName) {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(this);
            oos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ActivityStore load(String fileName) {
        ActivityStore data = null;
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);

            data = (ActivityStore)ois.readObject();
            ois.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return data;
    }
}

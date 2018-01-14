package com.bcit.jabe.facemorph;

import android.graphics.Bitmap;
import android.os.Bundle;

import java.io.Serializable;

/**
 * Created by Xeltide on 12/01/2018.
 */

public class ActivityStore implements Serializable {

    private Bitmap startFrame;
    private Bitmap endFrame;

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

    public void saveToBundle(Bundle bundle) {
        bundle.putParcelable("startFrame", startFrame);
        bundle.putParcelable("endFrame", endFrame);
    }

    public void loadFromBundle(Bundle bundle) {
        startFrame = bundle.getParcelable("startFrame");
        endFrame = bundle.getParcelable("endFrame");
    }
}

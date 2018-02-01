package com.bcit.jabe.facemorph;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Xeltide on 30/01/2018.
 */

public class SerializableBitmap implements Serializable {

    private int width;
    private int height;
    private int[] rawImageData;

    SerializableBitmap(Bitmap b) {
        this.width = b.getWidth();
        this.height = b.getHeight();
        rawImageData = new int[width * height];
        b.getPixels(rawImageData, 0, this.width,0,0, this.width, this.height);
    }

    public Bitmap getBitmap() {
        return Bitmap.createBitmap(rawImageData, 0, this.width, this.width, this.height, Bitmap.Config.ARGB_8888);
    }
}

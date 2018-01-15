package com.bcit.jabe.facemorph;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Xeltide on 12/01/2018.
 */

public class PhotoPreviewFragment extends Fragment {

    private final int PICK_IMAGE = 1;
    private boolean firstImage = true;
    private boolean enablePlusClick = true;
    private MainActivity activity;
    private ImageView imgPreview;
    private ImageButton imgPreview1Button;
    private ImageButton imgPreview2Button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photo_preview_view, container, false);
        activity = (MainActivity)getActivity();

        initImgPreview(view);
        initImgPreviewButtons(view);

        return view;
    }

    private void initImgPreview(View v) {
        imgPreview = v.findViewById(R.id.photoView);
        imgPreview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (enablePlusClick) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                }
            }
        });
    }

    private void initImgPreviewButtons(View v) {
        imgPreview1Button = v.findViewById(R.id.imgPreview1Button);
        imgPreview1Button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                firstImage = true;
                imgPreview1Button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.selected)));
                imgPreview2Button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.unselected)));
                if (activity.getStore().getStartFrame() != null) {
                    imgPreview.setImageBitmap(((MainActivity)getActivity()).getStore().getStartFrame());
                    enablePlusClick = false;
                } else {
                    Drawable myDrawable = getResources().getDrawable(R.drawable.ic_plus);
                    imgPreview.setImageDrawable(myDrawable);
                    enablePlusClick = true;
                }
            }
        });

        imgPreview2Button = v.findViewById(R.id.imgPreview2Button);
        imgPreview2Button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                firstImage = false;
                imgPreview2Button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.selected)));
                imgPreview1Button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.unselected)));
                if (activity.getStore().getEndFrame() != null) {
                    imgPreview.setImageBitmap(((MainActivity)getActivity()).getStore().getEndFrame());
                    enablePlusClick = false;
                } else {
                    Drawable myDrawable = getResources().getDrawable(R.drawable.ic_plus);
                    imgPreview.setImageDrawable(myDrawable);
                    enablePlusClick = true;
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        if (activity.getStore().getStartFrame() != null) {
            imgPreview.setImageBitmap(((MainActivity)getActivity()).getStore().getStartFrame());
            enablePlusClick = false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getApplicationContext().getContentResolver(), uri);
                    imgPreview.setImageBitmap(bitmap);
                    enablePlusClick = false;
                    if (firstImage) {
                        activity.getStore().setStartFrame(bitmap);
                    } else {
                        activity.getStore().setEndFrame(bitmap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

package com.bcit.jabe.facemorph;

import android.app.Fragment;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by Xeltide on 13/01/2018.
 */

public class PhotoEditFragment extends Fragment {

    private MainActivity activity;
    private ImageButton imgEdit1Button;
    private ImageButton imgEdit2Button;
    private PhotoEditView editView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photo_edit_view, container, false);
        activity = (MainActivity)getActivity();

        initImgEditButtons(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        editView = activity.findViewById(R.id.surfaceView);
        editView.setActivity(activity);
        editView.setCurrentImage(activity.getStore().getStartFrame());
    }

    private void initImgEditButtons(View v) {
        imgEdit1Button = v.findViewById(R.id.imgEdit1Button);
        imgEdit1Button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                activity.getStore().setFirstFrame(true);
                imgEdit1Button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.selected)));
                imgEdit2Button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.unselected)));
                editView.setCurrentImage(activity.getStore().getStartFrame());
            }
        });

        imgEdit2Button = v.findViewById(R.id.imgEdit2Button);
        imgEdit2Button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                activity.getStore().setFirstFrame(false);
                imgEdit2Button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.selected)));
                imgEdit1Button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.unselected)));
                editView.setCurrentImage(activity.getStore().getEndFrame());
            }
        });
    }
}

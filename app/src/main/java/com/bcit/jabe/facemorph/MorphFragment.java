package com.bcit.jabe.facemorph;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * Created by Xeltide on 23/01/2018.
 */

public class MorphFragment extends Fragment {

    private MainActivity activity;
    private ImageView frameView;
    private EditText numberField;
    private FloatingActionButton morphPlayFAB;
    private FloatingActionButton cycleRightFAB;
    private FloatingActionButton cycleLeftFAB;
    private ProgressBar progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.morph_view, container, false);
        activity = (MainActivity)getActivity();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        frameView = activity.findViewById(R.id.frameView);
        if (activity.getStore().getStartFrame() != null) {
            frameView.setImageBitmap(activity.getStore().getStartFrame());
        }

        numberField = activity.findViewById(R.id.frameCountField);
        numberField.setText(Integer.toString(activity.getStore().getFrameCount()));
        numberField.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (!numberField.getText().toString().isEmpty()) {
                        activity.getStore().setFrameCount(Integer.parseInt(numberField.getText().toString()));
                    } else {
                        numberField.setText(activity.getStore().getFrameCount());
                    }
                    return true;
                }
                return false;
            }
        });

        progress = activity.findViewById(R.id.progressBar);

        morphPlayFAB = activity.findViewById(R.id.morphPlayFAB);
        morphPlayFAB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if ((!activity.getStore().isFaceMorphed()) && (activity.getStore().getStartFrame() != null) && (activity.getStore().getEndFrame() != null)) {
                    MorphParentThread t = new MorphParentThread(activity.getStore(), progress, frameView);
                    t.start();
                    activity.getStore().setIsFaceMorphed(true);
                    progress.setVisibility(View.VISIBLE);
                } else if (activity.getStore().isFaceMorphed()) {

                }
            }
        });

        cycleRightFAB = activity.findViewById(R.id.cycleRightFAB);
        cycleRightFAB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                selectFrameRight();
            }
        });

        cycleLeftFAB = activity.findViewById(R.id.cycleLeftFAB);
        cycleLeftFAB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                selectFrameLeft();
            }
        });
    }

    private void selectFrameRight() {
        ActivityStore store = activity.getStore();
        int viewing = store.getViewingFrameNum();

        if (viewing == -1) {
            if (store.getFrameCount() > 0) {
                store.setViewingFrameNum(0);
                frameView.setImageBitmap(store.getFrame(0));
            } else {
                store.setViewingFrameNum(-2);
                frameView.setImageBitmap(store.getEndFrame());
            }
        } else if (viewing == -2) {
            store.setViewingFrameNum(-1);
            frameView.setImageBitmap(store.getStartFrame());
        } else {
            if (viewing < store.getFrameCount() - 1) {
                store.setViewingFrameNum(store.getViewingFrameNum() + 1);
                frameView.setImageBitmap(store.getFrame(store.getViewingFrameNum()));
            } else {
                store.setViewingFrameNum(-2);
                frameView.setImageBitmap(store.getEndFrame());
            }
        }
    }

    private void selectFrameLeft() {
        ActivityStore store = activity.getStore();
        int viewing = store.getViewingFrameNum();

        if (viewing == -1) {
            store.setViewingFrameNum(-2);
            frameView.setImageBitmap(store.getEndFrame());
        } else if (viewing == -2) {
            if (store.getFrameCount() > 0) {
                store.setViewingFrameNum(store.getFrameCount() - 1);
                frameView.setImageBitmap(store.getFrame(store.getViewingFrameNum()));
            } else {
                store.setViewingFrameNum(-1);
                frameView.setImageBitmap(store.getStartFrame());
            }
        } else {
            if (viewing > 0) {
                store.setViewingFrameNum(store.getViewingFrameNum() - 1);
                frameView.setImageBitmap(store.getFrame(store.getViewingFrameNum()));
            } else {
                store.setViewingFrameNum(-1);
                frameView.setImageBitmap(store.getStartFrame());
            }
        }
    }
}

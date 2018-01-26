package com.bcit.jabe.facemorph;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
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
    private boolean cyclingImages = false;

    private Handler handler;
    private Runnable playLoop;
    private int frameDelayMS = 250;

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

        handler = new Handler();
        playLoop = new Runnable() {

            @Override
            public void run() {
                try {
                    selectFrameRight();
                } finally {
                    handler.postDelayed(playLoop, frameDelayMS);
                }
            }
        };

        morphPlayFAB = activity.findViewById(R.id.morphPlayFAB);
        morphPlayFAB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if ((!activity.getStore().isFaceMorphed()) && (activity.getStore().getStartFrame() != null) && (activity.getStore().getEndFrame() != null)) {
                    MorphParentThread t = new MorphParentThread(activity.getStore(), progress, frameView);
                    t.start();
                    activity.getStore().setIsFaceMorphed(true);
                    progress.setVisibility(View.VISIBLE);
                    morphPlayFAB.setImageResource(R.drawable.play);
                } else if (activity.getStore().isFaceMorphed() && !cyclingImages) {
                    playLoop.run();
                    morphPlayFAB.setImageResource(R.drawable.pause);
                    cyclingImages = true;
                } else if (activity.getStore().isFaceMorphed() && cyclingImages) {
                    handler.removeCallbacks(playLoop);
                    morphPlayFAB.setImageResource(R.drawable.play);
                    cyclingImages = false;
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
            if (store.getFrameCount() > 0 && store.isFaceMorphed()) {
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
            if (viewing < store.getFrameCount() - 1 && store.isFaceMorphed()) {
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
            if (store.getFrameCount() > 0 && store.isFaceMorphed()) {
                store.setViewingFrameNum(store.getFrameCount() - 1);
                frameView.setImageBitmap(store.getFrame(store.getViewingFrameNum()));
            } else {
                store.setViewingFrameNum(-1);
                frameView.setImageBitmap(store.getStartFrame());
            }
        } else {
            if (viewing > 0 && store.isFaceMorphed()) {
                store.setViewingFrameNum(store.getViewingFrameNum() - 1);
                frameView.setImageBitmap(store.getFrame(store.getViewingFrameNum()));
            } else {
                store.setViewingFrameNum(-1);
                frameView.setImageBitmap(store.getStartFrame());
            }
        }
    }
}

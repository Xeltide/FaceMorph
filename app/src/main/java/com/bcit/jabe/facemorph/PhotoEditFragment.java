package com.bcit.jabe.facemorph;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Xeltide on 13/01/2018.
 */

public class PhotoEditFragment extends Fragment {

    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photo_edit_view, container, false);
        activity = (MainActivity)getActivity();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        PhotoEditView editView = activity.findViewById(R.id.surfaceView);
        editView.setActivity(activity);
    }
}

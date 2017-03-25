package com.hackathon.gezinti.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hackathon.gezinti.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BottomSheetListFragment extends Fragment {

    private View mDimBackground;

    public BottomSheetListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet_list, container, false);
    }

    public void setDimBackground(View dimBackground) {
        this.mDimBackground = dimBackground;
        dimBackground.setVisibility(View.GONE);
    }

    public BottomSheetBehavior.BottomSheetCallback getBottomSheetCallback() {
        return bottomSheetCallback;
    }


    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            mDimBackground.animate().alpha(slideOffset).setDuration(0).start();

            if (slideOffset > 0) {
                mDimBackground.setVisibility(View.VISIBLE);
            } else {
                mDimBackground.setVisibility(View.GONE);
            }
        }
    };

}

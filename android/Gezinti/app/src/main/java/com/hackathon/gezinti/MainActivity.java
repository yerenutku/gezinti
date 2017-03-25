package com.hackathon.gezinti;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hackathon.gezinti.fragment.BottomSheetListFragment;
import com.hackathon.gezinti.fragment.MapFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private BottomSheetBehavior<View> mBottomSheetBehavior;
    private BottomSheetListFragment mBottomSheetListFragment;

    private MapFragment mMapFragment;
    private Button mButtonRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        if(savedInstanceState == null) {
            mMapFragment = new MapFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_map_container, mMapFragment, "")
                    .commit();
        }

    }

    private void initViews() {
        mBottomSheetListFragment = (BottomSheetListFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentBottomSheet);
        mBottomSheetListFragment.setDimBackground(findViewById(R.id.dim_background));
        mBottomSheetBehavior  = BottomSheetBehavior.from(findViewById(R.id.fragmentBottomSheet));
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setBottomSheetCallback(mBottomSheetListFragment.getBottomSheetCallback());
        mButtonRefresh = (Button) findViewById(R.id.btn_refresh);
        mButtonRefresh.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mButtonRefresh.getId()){
            if(mMapFragment.isAdded()){
                mMapFragment.makeRequestAndPin();
            }
        }
    }

}

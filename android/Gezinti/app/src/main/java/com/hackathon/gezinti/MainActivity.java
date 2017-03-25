package com.hackathon.gezinti;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.hackathon.gezinti.fragment.BottomSheetListFragment;
import com.hackathon.gezinti.ui.MapFragment;

public class MainActivity extends AppCompatActivity {
    private BottomSheetBehavior<View> mBottomSheetBehavior;
    private BottomSheetListFragment mBottomSheetListFragment;

    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        if(savedInstanceState == null) {
            mapFragment = new MapFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_map_container, mapFragment, "")
                    .commit();
        }

    }

    private void initViews() {

        mBottomSheetListFragment = (BottomSheetListFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentBottomSheet);
        mBottomSheetListFragment.setDimBackground(findViewById(R.id.dim_background));
        mBottomSheetBehavior  = BottomSheetBehavior.from(findViewById(R.id.fragmentBottomSheet));
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setBottomSheetCallback(mBottomSheetListFragment.getBottomSheetCallback());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_search){
            if(mapFragment.isAdded()){
                mapFragment.makeRequestAndPin();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

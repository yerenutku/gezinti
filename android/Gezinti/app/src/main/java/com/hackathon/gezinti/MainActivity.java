package com.hackathon.gezinti;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hackathon.gezinti.fragment.BottomSheetListFragment;
import com.hackathon.gezinti.models.EventResponse;
import com.hackathon.gezinti.ui.MapFragment;

import java.util.ArrayList;
import java.util.List;

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
        List<EventResponse> eventResponseList = new ArrayList<>();
        eventResponseList.add(new EventResponse("eren"));
        eventResponseList.add(new EventResponse("ceren"));
        eventResponseList.add(new EventResponse("deren"));
        eventResponseList.add(new EventResponse("reren"));
        eventResponseList.add(new EventResponse("teren"));
        eventResponseList.add(new EventResponse("yeren"));
        eventResponseList.add(new EventResponse("keren"));
        eventResponseList.add(new EventResponse("peren"));
        mBottomSheetListFragment.setEventsForPosition(eventResponseList);

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

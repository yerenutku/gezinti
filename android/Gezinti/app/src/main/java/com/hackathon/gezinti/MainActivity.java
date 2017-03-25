package com.hackathon.gezinti;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hackathon.gezinti.fragment.BottomSheetListFragment;
import com.hackathon.gezinti.fragment.MapFragment;
import com.hackathon.gezinti.models.common.Coordinates;
import com.hackathon.gezinti.models.request.EventCreateRequest;
import com.hackathon.gezinti.models.response.EventCreateResponse;
import com.hackathon.gezinti.network.EventInteractor;

import java.util.ArrayList;
import java.util.List;

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

        EventInteractor interactor = new EventInteractor();
        EventCreateRequest eventCreateRequest = new EventCreateRequest();
        eventCreateRequest.setTitle("eren");
        eventCreateRequest.setDesc("açıklama");
        eventCreateRequest.setOwner("58d553e3ef54401bec32899d");
        eventCreateRequest.setTime("2018-05-18T16:00:00.000Z");
        ArrayList<Coordinates> coordinates = new ArrayList<Coordinates>();
        coordinates.add(new Coordinates("65.5","56.6"));
        coordinates.add(new Coordinates("31.5","32.6"));
        eventCreateRequest.setCoordinates(coordinates);
        interactor.eventCreate(this,eventCreateRequest);


    }

    private void initViews() {
        mBottomSheetListFragment = (BottomSheetListFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentBottomSheet);
        mBottomSheetListFragment.setDimBackground(findViewById(R.id.dim_background));
        mBottomSheetBehavior  = BottomSheetBehavior.from(findViewById(R.id.fragmentBottomSheet));
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setBottomSheetCallback(mBottomSheetListFragment.getBottomSheetCallback());
        List<EventCreateResponse> eventCreateResponseList = new ArrayList<>();
//        eventCreateResponseList.add(new EventCreateResponse("eren"));
//        eventCreateResponseList.add(new EventCreateResponse("ceren"));
//        eventCreateResponseList.add(new EventCreateResponse("deren"));
//        eventCreateResponseList.add(new EventCreateResponse("reren"));
//        eventCreateResponseList.add(new EventCreateResponse("teren"));
//        eventCreateResponseList.add(new EventCreateResponse("yeren"));
//        eventCreateResponseList.add(new EventCreateResponse("keren"));
//        eventCreateResponseList.add(new EventCreateResponse("peren"));
        mBottomSheetListFragment.setEventsForPosition(eventCreateResponseList);

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

package com.hackathon.gezinti.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;
import com.hackathon.gezinti.R;
import com.hackathon.gezinti.fragment.MapFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private MapFragment mMapFragment;
    private Button mButtonRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        if (savedInstanceState == null) {
            mMapFragment = MapFragment.getInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_map_container, mMapFragment, "")
                    .commit();
        }

    }

    private void initViews() {
        mButtonRefresh = (Button) findViewById(R.id.btn_refresh);
        mButtonRefresh.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mButtonRefresh.getId()) {
            if (mMapFragment.isAdded()) {
                findNearByEvents();
            }
        }
    }

    private void findNearByEvents() {
        //make network request with mapfragment.scenterofscreen and response is OnTempResponse now
        LatLng latLng = mMapFragment.getCenterOfScreen();
        double longitude = latLng.longitude;
        double latitude = latLng.latitude;

        //int spEventsPosition = mEventsSpinner.getSelectedItemPosition();
        //int spTimesPosition = mTimesSpinner.getSelectedItemPosition();

        Log.e("MainAct", "Lat: " + latitude + " Lon: " + longitude);
        /*
        mEventInteractor = new EventInteractor(this);
        EventSearchRequest eventSearchRequest = new EventSearchRequest();
        eventSearchRequest.setLat(String.valueOf(latitude));
        eventSearchRequest.setLon(String.valueOf(longitude));
        eventSearchRequest.setEventTime(spTimesPosition);
        eventSearchRequest.setEventType(spEventsPosition);
        mEventInteractor.eventSearch(eventSearchRequest, new EventSearchListener() {
            @Override
            public void onEventSearch(EventSearchResponse response) {
                mBottomSheetListFragment.refreshEvents(response.getEvents());
                mMapFragment.getEventsAndPin(response.getEvents());
            }

            @Override
            public void onError(String errorMessage) {
                showErrorMessage(errorMessage);
            }

            @Override
            public void onBeforeRequest() {
                showWaitingDialog();
            }

            @Override
            public void onAfterRequest() {
                dismissWaitingDialog();
            }
        });
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}

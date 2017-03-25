package com.hackathon.gezinti;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.hackathon.gezinti.fragment.BottomSheetListFragment;
import com.hackathon.gezinti.models.EventResponse;
import com.hackathon.gezinti.fragment.MapFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private BottomSheetBehavior<View> mBottomSheetBehavior;
    private BottomSheetListFragment mBottomSheetListFragment;

    private MapFragment mMapFragment;
    private Button mButtonRefresh;
    private Spinner mEventsSpinner, mTimesSpinner;

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
        List<EventResponse> eventResponseList = new ArrayList<>();
        eventResponseList.add(new EventResponse("eren", 41.0791894,29.0180006));
        eventResponseList.add(new EventResponse("ceren", 41.0291894,29.0380006));
        eventResponseList.add(new EventResponse("deren", 41.0891894,29.0480006));
        eventResponseList.add(new EventResponse("reren", 41.0491894,29.0280006));
        eventResponseList.add(new EventResponse("teren", 41.0591894,29.1180006));
        eventResponseList.add(new EventResponse("yeren", 41.1391894,29.0980006));
        eventResponseList.add(new EventResponse("keren", 41.1791894,29.0120006));
        eventResponseList.add(new EventResponse("peren", 41.0793594,29.0220006));
        mBottomSheetListFragment.setEventsForPosition(eventResponseList);

        mButtonRefresh = (Button) findViewById(R.id.btn_refresh);
        mButtonRefresh.setOnClickListener(this);

        mEventsSpinner = (Spinner) findViewById(R.id.sp_events);
        ArrayAdapter<CharSequence> eventAdapter = ArrayAdapter
                .createFromResource(this, R.array.array_event_types,
                        android.R.layout.simple_spinner_item);
        eventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEventsSpinner.setAdapter(eventAdapter);

        mTimesSpinner = (Spinner) findViewById(R.id.sp_times);
        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter
                .createFromResource(this, R.array.array_event_times,
                        android.R.layout.simple_spinner_item);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTimesSpinner.setAdapter(timeAdapter);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mButtonRefresh.getId()){
            if(mMapFragment.isAdded()){

                //make network request with mapfragment.scenterofscreen and response is OnTempResponse now
                LatLng latLng = mMapFragment.getCenterOfScreen();
                double longitude = latLng.longitude;
                double latitude = latLng.latitude;

                int spEventsPosition = mEventsSpinner.getSelectedItemPosition();
                int spTimesPosition = mTimesSpinner.getSelectedItemPosition();

                Log.e("MainAct", "Lat: "+latitude + " Lon: " + longitude);

                OnTempResponse();

            }
        }
    }

    private void OnTempResponse(){
        List<EventResponse> eventResponseList = new ArrayList<>();
        eventResponseList.add(new EventResponse("test-coord", 41.0824866,29.0210442));
        eventResponseList.add(new EventResponse("test-coord2", 41.0825866,29.0210442));
        eventResponseList.add(new EventResponse("test-coord3", 41.0824866,29.0210442));
        eventResponseList.add(new EventResponse("test-coord4", 41.0824766,29.0210442));
        eventResponseList.add(new EventResponse("test-coord5", 41.0825866,29.0210442));
        eventResponseList.add(new EventResponse("test-coord6", 41.0822866,29.0210442));
        eventResponseList.add(new EventResponse("test-coord7", 41.0814866,29.0210442));
        eventResponseList.add(new EventResponse("test-coord8", 41.0824876,29.0210442));
        mBottomSheetListFragment.refreshEvents(eventResponseList);
        mMapFragment.getEventsAndPin(eventResponseList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_search){
            try {
                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                .build(this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            } catch (GooglePlayServicesRepairableException e) {
                // TODO: Handle the error.
            } catch (GooglePlayServicesNotAvailableException e) {
                // TODO: Handle the error.
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.d("MainAct", place.getLatLng().latitude + " " + place.getLatLng().longitude );
                mMapFragment.moveMapsCamera(place.getLatLng());

                // TODO: MAKE NETWORK REQUEST - USE getLatLng

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("MainAct", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}

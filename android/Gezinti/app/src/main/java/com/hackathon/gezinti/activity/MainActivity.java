package com.hackathon.gezinti.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
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
import com.hackathon.gezinti.R;
import com.hackathon.gezinti.fragment.BottomSheetListFragment;
import com.hackathon.gezinti.fragment.MapFragment;
import com.hackathon.gezinti.interfaces.EventSearchListener;
import com.hackathon.gezinti.models.common.Event;
import com.hackathon.gezinti.models.request.EventSearchRequest;
import com.hackathon.gezinti.models.response.EventSearchResponse;
import com.hackathon.gezinti.network.EventInteractor;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener, BottomSheetListFragment.BottomSheetInteraction {
    private BottomSheetBehavior<View> mBottomSheetBehavior;
    private BottomSheetListFragment mBottomSheetListFragment;

    private MapFragment mMapFragment;
    private Button mButtonRefresh;
    private Spinner mEventsSpinner, mTimesSpinner;
    private EventInteractor mEventInteractor;
    private static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    public static int EVENT_DETAIL_REQUEST_CODE = 101;

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
        mBottomSheetListFragment = (BottomSheetListFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentBottomSheet);
        mBottomSheetListFragment.setDimBackground(findViewById(R.id.dim_background));
        mBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.fragmentBottomSheet));
        mBottomSheetBehavior.setHideable(false);
        mBottomSheetBehavior.setBottomSheetCallback(mBottomSheetListFragment.getBottomSheetCallback());

        ArrayList<Event> events = new ArrayList<>();
        mBottomSheetListFragment.setEventsForPosition(events);

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

        int spEventsPosition = mEventsSpinner.getSelectedItemPosition();
        int spTimesPosition = mTimesSpinner.getSelectedItemPosition();

        Log.e("MainAct", "Lat: " + latitude + " Lon: " + longitude);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
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
                Log.d("MainAct", place.getLatLng().latitude + " " + place.getLatLng().longitude);
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
        if (requestCode == EVENT_DETAIL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                findNearByEvents();
            }
        }
    }

    @Override
    public void onEventCreated() {
        findNearByEvents();
    }
}

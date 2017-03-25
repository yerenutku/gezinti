package com.hackathon.gezinti.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hackathon.gezinti.R;
import com.hackathon.gezinti.models.EventResponse;

import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMarkerClickListener{

    /*  Required for Google Maps  */
    private MapView mapView;
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private LocationRequest mLocationRequest;

    private boolean getLocation = true;

    private static final String[] FINE_LOCATION={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private final int MY_LOCATION_REQUEST_CODE = 0001;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        mapView = (MapView) view.findViewById(R.id.mv_maps);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    FINE_LOCATION[0])
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                googleMap.setMyLocationEnabled(true);
            } else {
                ActivityCompat.requestPermissions(getActivity(), FINE_LOCATION, MY_LOCATION_REQUEST_CODE);
            }
        }
        else {
            buildGoogleApiClient();
            googleMap.setMyLocationEnabled(true);
        }

        makeRequestAndPin();
    }



    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                FINE_LOCATION[0])
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Focus current location
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        //move map camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public void getEventsAndPin(List<EventResponse> eventResponseList){
        this.googleMap.clear();
        for(EventResponse item : eventResponseList){
            this.googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(item.getLatitude(), item.getLongitude()))
                    .title(item.getText())
            );
        }
    }

    public LatLng getCenterOfScreen(){
        return this.googleMap.getCameraPosition().target;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }


    //deprecated
    public void makeRequestAndPin(){

        if (getLocation) {
            this.googleMap.clear();
            LatLng tempCoordinat = new LatLng(41.08, 29.02);
            this.googleMap.addMarker(new MarkerOptions().position(tempCoordinat)
                    .title("Test - 1"));
            LatLng tempCoordinat2 = new LatLng(40.08, 29.02);
            this.googleMap.addMarker(new MarkerOptions().position(tempCoordinat2)
                    .title("Test - 2"));
        } else {
            this.googleMap.clear();
            LatLng tempCoordinat = new LatLng(41.08, 29.22);
            this.googleMap.addMarker(new MarkerOptions().position(tempCoordinat)
                    .title("Test - 3"));
            LatLng tempCoordinat2 = new LatLng(40.08, 29.32);
            this.googleMap.addMarker(new MarkerOptions().position(tempCoordinat2)
                    .title("Test - 4"));
        }
        getLocation = !getLocation;

    }

    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                FINE_LOCATION[0])
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    FINE_LOCATION[0])) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        FINE_LOCATION,
                        MY_LOCATION_REQUEST_CODE);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        FINE_LOCATION,
                        MY_LOCATION_REQUEST_CODE);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_LOCATION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            FINE_LOCATION[0])
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        googleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(getContext(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if(mapView != null) mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mapView != null) mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mapView != null) mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if(mapView != null) mapView.onLowMemory();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}

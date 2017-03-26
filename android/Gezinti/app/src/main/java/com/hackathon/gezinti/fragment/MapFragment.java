package com.hackathon.gezinti.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.hackathon.gezinti.EventDetailActivity;
import com.hackathon.gezinti.MainActivity;
import com.hackathon.gezinti.R;
import com.hackathon.gezinti.models.common.Coordinates;
import com.hackathon.gezinti.models.common.Event;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapLongClickListener {

    public static MapFragment instance;

    /*  Required for Google Maps  */
    private MapView mapView;
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private LocationRequest mLocationRequest;

    private ArrayList<Event> mEvents;
    private boolean getLocation = true;

    private static final String[] FINE_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private final int MY_LOCATION_REQUEST_CODE = 0001;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment getInstance(){
        if(instance == null)
            instance = new MapFragment();
        return instance;
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
        } else {
            buildGoogleApiClient();
            googleMap.setMyLocationEnabled(true);
        }

        //def camera position
        LatLng latLng = new LatLng(41.0823884, 29.0191999);
        moveMapsCamera(latLng);

        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setOnMapLongClickListener(this);
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

    public void getEventsAndPin(ArrayList<Event> events) {
        mEvents = events;
        googleMap.clear();
        Marker marker;

        int count = 0;
        for (Event item : events) {
            if (item.getCoordinatesList().size() > 1) {
                Polygon polygon = googleMap.addPolygon(new PolygonOptions()
                    .addAll(coordinateToLatLng(item.getCoordinatesList()))
                    .strokeColor(0xFF00AA00)
                    .fillColor(0x2200FFFF)
                    .strokeWidth(2)
                );
                polygon.setTag(count);
                count++;
                polygon.setClickable(true);
                googleMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
                    @Override
                    public void onPolygonClick(Polygon polygon) {
                        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("EventDetail", mEvents.get((int) polygon.getTag()));
                        intent.putExtras(bundle);
                        getActivity().startActivityForResult(intent, MainActivity.EVENT_DETAIL_REQUEST_CODE);
                    }
                });
            } else {
                marker = this.googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.valueOf(item.getCoordinatesList().get(0).getLat()), Double.valueOf(item.getCoordinatesList().get(0).getLon())))
                        .title(item.getTitle())
                );
                marker.setTag(count);
                count++;
            }
        }
    }

    public LatLng getCenterOfScreen() {
        return googleMap.getCameraPosition().target;
    }

    public void moveMapsCamera(LatLng latLng) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.d("MapFragment", marker.getTag().toString());
        mEvents.get((int) marker.getTag());

        //goto eventdetailactivity
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("EventDetail", mEvents.get((int) marker.getTag()));
        intent.putExtras(bundle);
        getActivity().startActivityForResult(intent, MainActivity.EVENT_DETAIL_REQUEST_CODE);
    }

    public List<LatLng> coordinateToLatLng(List<Coordinates> coordinatesList){
        List<LatLng> latLngList = new ArrayList<>();
        LatLng tempLatLng;
        for(Coordinates item : coordinatesList){
            tempLatLng = new LatLng(Double.parseDouble(item.getLat()), Double.parseDouble(item.getLon()));
            latLngList.add(tempLatLng);
        }
        return latLngList;
    }


    //
    //  Event Create Methods
    //
    List<Marker> mMarkerList;
    ArrayList<Coordinates> mCoordinatesList;
    boolean mCreatorMode = false;
    public void openEventCreatorMode(){
        googleMap.clear();
        if(mCoordinatesList == null) {
            mCoordinatesList = new ArrayList<>();
            mMarkerList = new ArrayList<>();
        }
        mCoordinatesList.clear();
        mMarkerList.clear();
        mCreatorMode = true;
    }
    public void closeEventCreatorMode(){
        googleMap.clear();
        mCoordinatesList.clear();
        mMarkerList.clear();
        mCreatorMode = false;
    }
    private Marker tempMarker;
    @Override
    public void onMapLongClick(LatLng latLng) {
        if(mCreatorMode){
            tempMarker = this.googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(""+mCoordinatesList.size())
            );
            mMarkerList.add(tempMarker);
            mCoordinatesList.add(new Coordinates(String.valueOf(latLng.latitude), String.valueOf(latLng.longitude)));
        }
    }
    public void popCoordinate(){
        if(mCoordinatesList != null && mCoordinatesList.size() > 0){
            mCoordinatesList.remove(mCoordinatesList.size()-1);
            Marker temp = mMarkerList.get(mMarkerList.size()-1);
            mMarkerList.remove(mMarkerList.size()-1);
            temp.remove();
        }
    }
    public ArrayList<Coordinates> getCoordinatesList(){
        if(mCoordinatesList != null && mCoordinatesList.size()>0)
            return mCoordinatesList;
        return null;
    }
    //
    //  Event Create Methods
    //



    //deprecated
    public void makeRequestAndPin() {

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
    public boolean checkLocationPermission() {
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
        if (mapView != null) mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) mapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) mapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) mapView.onLowMemory();
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}

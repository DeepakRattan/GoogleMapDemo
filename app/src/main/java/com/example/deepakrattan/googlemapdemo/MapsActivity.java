package com.example.deepakrattan.googlemapdemo;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


//This GoogleMap project demonstrates how to add market at current location.Also how to add a marker after dragging
// and on long clicking the map

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private Button btnCurrentLocation;
    private GoogleApiClient googleApiClient;
    private double latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //When you want to make a connection to one of the Google APIs provided in the Google Play services
        // library (such as Google Sign-In, Games, or Drive), you need to create an
        // instance of GoogleApiClient ("Google API Client"). The Google API Client
        // provides a common entry point to all the Google Play services and manages the
        // network connection between the user's device and each Google service.

        //Initializing googleApiClient
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        //findViewByID
        btnCurrentLocation = (Button) findViewById(R.id.btnCurrentLocation);

        //Adding onClickListener
        btnCurrentLocation.setOnClickListener(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCurrentLocation:
                getCurrentLocation();

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }


    //Getting Current Location
    public void getCurrentLocation() {
        mMap.clear();

        //Creating a location object
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        //Moving the map to location
        moveMap();
    }

    //To move the map to a specified location
    public void moveMap() {

        LatLng latLng = new LatLng(latitude, longitude);

        //Adding marker
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Current Location").draggable(true));

        //Moving the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        //Animating the camera
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

        //Getting the coordinates
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;

        //Moving the map
        moveMap();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.clear();

        //Adding a new marker to the current pressed position
        latitude = latLng.latitude;
        longitude = latLng.longitude;
        moveMap();
    }
}

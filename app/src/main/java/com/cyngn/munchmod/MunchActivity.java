package com.cyngn.munchmod;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;


import com.cyngn.munchmod.data.CurrentLocationClient;
import com.cyngn.munchmod.data.YelpApiClient;
import com.cyngn.munchmod.utils.LatLngUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yelp.clientlib.entities.Business;

import java.util.List;


public class MunchActivity extends FragmentActivity implements
        OnMapReadyCallback,
        CurrentLocationClient.ResultCallback,
        YelpApiClient.ResultCallback
{

    private static final String TAG = "MunchActivity";

    private GoogleMap mMap;
    private YelpApiClient mYelpApiClient;
    private CurrentLocationClient mCurrentLocationClient;

    private LatLng mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mYelpApiClient = ((MunchApp)getApplication()).getYelpApiClient();
        mCurrentLocationClient = ((MunchApp)getApplication()).getCurrentLocationClient();

        setContentView(R.layout.activity_munch);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mCurrentLocationClient.addListener(this);
        mYelpApiClient.addListener(this);

    }
    @Override
    protected void onStart() {
        super.onStart();
        //TODO
    }

    @Override
    protected void onStop() {
        super.onStop();
        //TODO:

    }

    @Override
    protected void onPause() {
        super.onPause();
        mCurrentLocationClient.removeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentLocationClient.addListener(this);
    }


    public void onCurrentLocation(Location location) {
        //TODO: set current location on map
        mCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        if (mMap != null) {
            showLocation(mCurrentLocation);
        }
    }

    private static final double SEARCH_RADIUS = 5000;
    private void showLocation(LatLng latLng) {
        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        mYelpApiClient.loadPlaces(LatLngUtils.toBounds(latLng, SEARCH_RADIUS));
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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        /*
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnCameraChangeListener(this);
        mMap.setOnMapClickListener(this); */
        try {
            mMap.setMyLocationEnabled(true);
        }
        catch (SecurityException se) {
            Log.d(TAG, "SecurityException for mMap.setMyLocationEnabled");
        }
        final UiSettings settings = mMap.getUiSettings();
        settings.setMyLocationButtonEnabled(true);
        settings.setZoomControlsEnabled(true);
        settings.setRotateGesturesEnabled(false);
        settings.setTiltGesturesEnabled(false);

        if (mCurrentLocation != null) {
            showLocation(mCurrentLocation);
        }
    }


    /**
     * Yelp Businesses received
     * @param businesses
     */
    @Override
    public void onBusinessesLoaded(List<Business> businesses) {
        Log.d(TAG, "onBusinessesLoaded received " + businesses.size() + " businesses");
        for (Business business : businesses) {
            //TODO: show these in list view and map view
            Log.d(TAG, "Business: " + business.name());
        }
    }
}

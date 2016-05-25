package com.cyngn.munchmod;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;


import com.cyngn.munchmod.data.CurrentLocationClient;
import com.cyngn.munchmod.data.YelpApiClient;
import com.cyngn.munchmod.utils.LocationUtils;
import com.google.android.gms.maps.model.LatLng;
import com.yelp.clientlib.entities.Business;

import java.util.List;


public class MunchActivity extends FragmentActivity implements
        MapFragment.MapLocationChangeListener,
        YelpApiClient.ResultCallback
{
    private static final boolean DEBUG = false;
    private static final String TAG = "MunchActivity";

    private static final double SEARCH_RADIUS = 5000; //I think this is 5km?

    private YelpApiClient mYelpApiClient;
    private CurrentLocationClient mCurrentLocationClient;
    private MapFragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (DEBUG) {
            Log.d(TAG, "onCreate");
        }
        super.onCreate(savedInstanceState);

        mYelpApiClient = ((MunchApp)getApplication()).getYelpApiClient();
        mCurrentLocationClient = ((MunchApp)getApplication()).getCurrentLocationClient();
        mCurrentLocationClient.requestPermissions(this);

        setContentView(R.layout.activity_munch);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mMapFragment = (MapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mMapFragment.setLocationListener(this);
    }

    @Override
    protected void onStart() {
        if (DEBUG) {
            Log.d(TAG, "onStart");
        }
        super.onStart();
        //TODO
    }

    @Override
    protected void onStop() {
        if (DEBUG) {
            Log.d(TAG, "onStop");
        }
        super.onStop();
        //TODO:
    }

    @Override
    protected void onPause() {
        if (DEBUG) {
            Log.d(TAG, "onPause");
        }
        super.onPause();
        //TODO
    }

    @Override
    protected void onResume() {
        if (DEBUG) {
            Log.d(TAG, "onResume");
        }
        super.onResume();
        //TODO
    }


    @Override
    public void onMapLocationChanged(LatLng latLng) {
        mYelpApiClient.loadPlaces(LocationUtils.toBounds(latLng, SEARCH_RADIUS));
    }

    @Override
    public void onBusinessesLoaded(List<Business> businesses) {
        mMapFragment.showBusinesses(businesses);
    }

}

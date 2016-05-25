package com.cyngn.munchmod.data;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;

/**
 * Current Location Client
 *
 * TODO: add a listener and make it return the current location
 */
public class CurrentLocationClient implements
        com.google.android.gms.location.LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "CurrentLocationLoader";

    public interface ResultCallback {
        void onCurrentLocation(Location location);
    }

    /**
     * Request Location Updates
     */
    private static final LocationRequest LOCATION_REQUEST = LocationRequest.create()
            .setInterval(5)         // 5 seconds
            .setFastestInterval(16)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


    private Context mContext;
    private List<ResultCallback> mCallbacks = new ArrayList<>();
    private GoogleApiClient mLocationClient = null;

    public CurrentLocationClient(Context context) {
        mContext = context;
        mLocationClient = new GoogleApiClient.Builder(mContext)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void addListener(CurrentLocationClient.ResultCallback callback) {
        if (mCallbacks.size() == 0) {

        }
    }

    public void removeListener(CurrentLocationClient.ResultCallback callback) {
        //TODO: stop getting current location
    }

    /**
     * Wrapper State change Methods
     */
    void connect() {

        if (!mLocationClient.isConnected() &&
                !mLocationClient.isConnecting()) {
            Log.d(TAG, "connect");

            mLocationClient.connect();
        }
    }

    void requestLocation() {
        /*
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }*/
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mLocationClient, LOCATION_REQUEST, this);
        } catch (SecurityException se) {

        }
    }
    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "CurrentLocationLoader: onConnected: " + bundle);
        requestLocation();

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: " + i);
    }

    /**
     * Implementation of {@link android.location.LocationListener}.
     */
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged received location=" + location);
        if (location != null) {
            //TODO: figure out if
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " + connectionResult);
        //postError(LocationObjects.LocationError.LOCATION_CLIENT_CONNECT);
    }


}

package com.cyngn.munchmod.data;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.cyngn.munchmod.MunchApp;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.HashSet;
import java.util.Set;


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

    // Permissions
    private static final String[] PERMISSIONS = new String[] {
        Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
    };

    /**
     * Request Location Updates
     */
    private static final LocationRequest LOCATION_REQUEST = LocationRequest.create()
            .setInterval(5)         // 5 seconds
            .setFastestInterval(16)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


    private Context mContext;
    private GoogleApiClient mLocationClient = null;
    private Location mLastLocation = null;

    private boolean mNotifying = false;
    private Set<ResultCallback> mCallbacks = new HashSet<>();

    public interface ResultCallback {
        void onCurrentLocation(Location location);
    }

    public CurrentLocationClient(Context context) {
        mContext = context;
        mLocationClient = new GoogleApiClient.Builder(mContext)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    /**
     * requestPermissions: activity should call this when using CurrentLocationClient
     * @param activity
     */
    public int requestPermissions(Activity activity) {
        final int requestCode = MunchApp.REQUEST_CODE_CURRENT_LOCATION_PERMISSIONS;
        ActivityCompat.requestPermissions(activity, PERMISSIONS, requestCode);
        return requestCode;
    }


    public void addListener(final CurrentLocationClient.ResultCallback callback) {
        if (mCallbacks.size() == 0) {
            connect();
        }
        if (mNotifying) {
            ((MunchApp)mContext.getApplicationContext()).getMainHandler().post(
                    new Runnable() {
                        @Override
                        public void run() {
                            mCallbacks.add(callback);
                        }
                    }
            );
        } else {
            mCallbacks.add(callback);
        }
    }

    public void removeListener(final CurrentLocationClient.ResultCallback callback) {

        if (mCallbacks.size() == 1) {
            disconnect();
        }

        if (mNotifying) {
            ((MunchApp)mContext.getApplicationContext()).getMainHandler().post(
                    new Runnable() {
                        @Override
                        public void run() {
                            mCallbacks.remove(callback);
                        }
                    }
            );
        } else {
            mCallbacks.remove(callback);
        }
    }

    private void notifyListeners() {
        mNotifying = true;
        for (ResultCallback callback : mCallbacks) {
            callback.onCurrentLocation(mLastLocation);
        }
        mNotifying = false;
    }

    /**
     * Wrapper State change Methods
     */
    private void connect() {
        if (!mLocationClient.isConnected() &&
                !mLocationClient.isConnecting()) {
            Log.d(TAG, "connect");

            mLocationClient.connect();
        }
    }

    private void disconnect() {
        if (mLocationClient != null) {
            Log.d(TAG, "disconnect");
            mLocationClient.disconnect();
        }
    }

    private void requestLocation() {

        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mLocationClient, LOCATION_REQUEST, this);
        } catch (SecurityException se) {
            Log.d(TAG, "calling activity must call CurrentLocationClient.requestPermissions " +
                    "before adding itself as a listener " + se);
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
            mLastLocation = location;
            notifyListeners();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " + connectionResult);
        //postError(LocationObjects.LocationError.LOCATION_CLIENT_CONNECT);
    }


}

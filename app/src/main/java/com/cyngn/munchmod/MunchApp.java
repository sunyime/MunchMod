package com.cyngn.munchmod;

import android.app.Application;
import android.os.Handler;

import com.cyngn.munchmod.data.CurrentLocationClient;
import com.cyngn.munchmod.data.YelpApiClient;

/**
 * The MunchConstants Application
 *
 * TODO: put any global data here
 */
public class MunchApp extends Application {

    private YelpApiClient mYelpApiClient;
    private CurrentLocationClient mCurrentLocationClient;
    private Handler mMainHandler;

    // Shared request codes for activities
    public static final int REQUEST_CODE_CURRENT_LOCATION_PERMISSIONS = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        mMainHandler = new Handler(getMainLooper());
        mYelpApiClient = new YelpApiClient(this);
        mCurrentLocationClient = new CurrentLocationClient(this);
    }

    /**
     * Get the Yelp Api client
     * @return
     */
    public YelpApiClient getYelpApiClient() {
        return mYelpApiClient;
    }

    /**
     * Get the current location client
     * @return
     */
    public CurrentLocationClient getCurrentLocationClient() {
        return mCurrentLocationClient;
    }

    /**
     * Get a main handler for posting runnables
     * @return
     */
    public Handler getMainHandler() {
        return mMainHandler;
    }
}

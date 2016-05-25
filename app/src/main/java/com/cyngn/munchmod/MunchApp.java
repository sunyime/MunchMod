package com.cyngn.munchmod;

import android.app.Application;

import com.cyngn.munchmod.data.CurrentLocationClient;
import com.cyngn.munchmod.data.YelpApiClient;

/**
 * The Munch Application
 *
 * TODO: put any global data here
 */
public class MunchApp extends Application {

    private YelpApiClient mYelpApiClient;
    private CurrentLocationClient mCurrentLocationClient;

    @Override
    public void onCreate() {
        super.onCreate();

        mYelpApiClient = new YelpApiClient(this);
        mCurrentLocationClient = new CurrentLocationClient(this);
    }

    public YelpApiClient getYelpApiClient() {
        return mYelpApiClient;
    }

    public CurrentLocationClient getCurrentLocationClient() {
        return mCurrentLocationClient;
    }
}

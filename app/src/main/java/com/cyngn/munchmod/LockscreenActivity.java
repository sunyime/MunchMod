package com.cyngn.munchmod;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.cyngn.munchmod.data.CurrentLocationClient;
import com.cyngn.munchmod.data.YelpApiClient;
import com.cyngn.munchmod.utils.AndroidUtils;
import com.google.android.gms.maps.model.LatLng;


/**
 * Activity from Lockscreen
 */
public class LockscreenActivity extends FragmentActivity implements
        CurrentLocationClient.ResultCallback,
        ViewPager.OnPageChangeListener
{
    private static final boolean DEBUG = true;
    private static final String TAG = "LockscreenActivity";

    private YelpApiClient mYelpApiClient;
    private CurrentLocationClient mCurrentLocationClient;
    //
    private ViewPager mLockscreenPager;
    private LockscreenPagerAdapter mLockscreenAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (DEBUG) {
            Log.d(TAG, "onCreate");
        }
        super.onCreate(savedInstanceState);

        mYelpApiClient = ((MunchApp)getApplication()).getYelpApiClient();
        mCurrentLocationClient = ((MunchApp)getApplication()).getCurrentLocationClient();
        mCurrentLocationClient.requestPermissions(this);

        setContentView(R.layout.activity_lockscreen);

        // Initialize Lockscreen view
        mLockscreenPager = (ViewPager) findViewById(R.id.lockscreen_pager);
        mLockscreenAdapter = new LockscreenPagerAdapter(getSupportFragmentManager());
        mLockscreenPager.setAdapter(mLockscreenAdapter);
        mYelpApiClient.addListener(mLockscreenAdapter);

        mCurrentLocationClient.addListener(this);
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
    public void onCurrentLocation(Location location) {
        mYelpApiClient.loadPlaces(this, new LatLng(location.getLatitude(), location.getLongitude()));
        mCurrentLocationClient.removeListener(this);

        Log.d(TAG, "Width=" + mLockscreenPager.getWidth() + " Height=" + mLockscreenPager.getHeight());
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (DEBUG) {
            Log.d(TAG, "onPageScrolled: offset " + positionOffset);
        }
        if (position == mLockscreenAdapter.getCount() - 1) {
            if (positionOffset > mLockscreenPager.getWidth()/3) {
                onShowMore(mLockscreenPager);
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (DEBUG) {
            Log.d(TAG, "onPageSelected " + position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (DEBUG) {
            Log.d(TAG, "onPageScrollStateChanged " + state);
        }
    }

    public void onShowMore(View v) {
        Intent intent = new Intent(this, MunchActivity.class);
        AndroidUtils.startActivity(this, intent);
    }

}

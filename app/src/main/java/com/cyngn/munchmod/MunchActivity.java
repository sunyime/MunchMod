package com.cyngn.munchmod;

import android.animation.AnimatorInflater;
import android.animation.FloatArrayEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.support.v4.view.ViewPager;


import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cyngn.munchmod.data.CurrentLocationClient;
import com.cyngn.munchmod.data.YelpApiClient;
import com.cyngn.munchmod.utils.LocationUtils;
import com.google.android.gms.maps.model.LatLng;
import com.yelp.clientlib.entities.Business;

import java.util.List;


public class MunchActivity extends FragmentActivity implements
        MapFragment.MapListener,
        YelpApiClient.ResultCallback,
        ViewPager.OnPageChangeListener
{
    private static final boolean DEBUG = false;
    private static final String TAG = "MunchActivity";

    private static final long DURATION_MAP_SPLASH_CROSSFADE_MS = 450;


    private YelpApiClient mYelpApiClient;
    private CurrentLocationClient mCurrentLocationClient;
    private LatLng mLastSearchLatLng = null;

    private ViewGroup mSplash;

    // map components
    private MapFragment mMapFragment;
    private ViewPager mBusinessPager;
    private BusinessPagerAdapter mBusinessAdapter;
    ValueAnimator animator ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (DEBUG) {
            Log.d(TAG, "onCreate");
        }
        super.onCreate(savedInstanceState);

        mYelpApiClient = ((MunchApp)getApplication()).getYelpApiClient();
        mYelpApiClient.addListener(this);
        mCurrentLocationClient = ((MunchApp)getApplication()).getCurrentLocationClient();
        mCurrentLocationClient.requestPermissions(this);

        setContentView(R.layout.activity_munch);

        // Initialize map + detail view
        mMapFragment = (MapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMapFragment.setLocationListener(this);
        mBusinessPager = (ViewPager)findViewById(R.id.pager);
        mBusinessAdapter = new BusinessPagerAdapter(getSupportFragmentManager());
        mBusinessPager.setAdapter(mBusinessAdapter);
        mBusinessPager.setOnPageChangeListener(this);
        mYelpApiClient.addListener(mBusinessAdapter);

        // Initialize Splash View
        mSplash = (ViewGroup) findViewById(R.id.splash);
        TextView splashText = (TextView) findViewById(R.id.splash_text);
        MunchCustomizer.setSplashText(splashText);


        animator = (ValueAnimator) AnimatorInflater.loadAnimator(this,R.animator.slide_in);
        final int height = getResources().getDimensionPixelSize(R.dimen.business_pager_height);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cur = animation.getAnimatedFraction();
                int cur_height = (int)(height*cur);
                mBusinessPager.getLayoutParams().height = cur_height;
                mMapFragment.setMapIconPadding(cur_height);
                mBusinessPager.requestLayout();
            }
        });
       /* final String action = getIntent().getAction();
        if (action != null) {
            TextView splashText = (TextView) findViewById(R.id.splash_text);
            MunchCustomizer.setSplashText(splashText);
        } else {
            mSplash.setVisibility(View.GONE);
        } */
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


    /**
     * Called by MapFragment to trigger a
     */
    void clearLastLocation() {
        mLastSearchLatLng = null;
    }

    @Override
    public void onMapLocationChanged(LatLng latLng) {
        if (DEBUG) {
            Log.d(TAG, "onMapLocationChanged");
        }

        if (mLastSearchLatLng == null /*||
                LocationUtils.computeDistance(mLastSearchLatLng, latLng) > SEARCH_RADIUS * 2 */) {
            mLastSearchLatLng = latLng;
            mYelpApiClient.loadPlaces(this, latLng);

            if (isSplashVisible()) {
                mMapFragment.animateIn(DURATION_MAP_SPLASH_CROSSFADE_MS);
                mSplash.animate().setDuration(DURATION_MAP_SPLASH_CROSSFADE_MS).alpha(0f);
            }
        }
    }

    private boolean isSplashVisible() {
        return mSplash.getVisibility() == View.VISIBLE && mSplash.getAlpha() > 0;
    }


    @Override
    public void onBusinessesLoaded(List<Business> businesses) {
        mMapFragment.showBusinesses(businesses);
    }


    @Override
    public void onMapClicked() {
        // mBusinessPager.animate().setDuration(DURATION_LIST_FADE_MS).alpha(0);
        if(mBusinessPager.getVisibility() == View.GONE) return;
        mBusinessPager.setVisibility(View.GONE);
        animator.reverse();
    }

    @Override
    public void onMapItemClicked(Business business) {
        showBusinessDetails(business);
    }


    private void showBusinessDetails(Business business) {

        if (DEBUG) {
            Log.d(TAG, "showBusinessDetails");
        }
        int pos = mYelpApiClient.getPositionForBusiness(business);
        if(pos < 0) return;
        mBusinessPager.setCurrentItem(pos, true);
        if(mBusinessPager.getVisibility() == View.VISIBLE) return;

        mBusinessPager.setVisibility(View.VISIBLE);


        Log.d(TAG, "starting animation");
        animator.start();

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (DEBUG) {
            Log.d(TAG, "onPageScrolled");
        }
        final Business business = mYelpApiClient.getBusinessAt(position);
        if (business != null) {
            mMapFragment.selectBusiness(business);
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

}

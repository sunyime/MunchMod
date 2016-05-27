package com.cyngn.munchmod;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.support.v4.view.ViewPager;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

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

    private static final double SEARCH_RADIUS = 5000; //I think this is 5km?
    private static final long DURATION_MAP_SPLASH_CROSSFADE_MS = 450;
    private static final long DURATION_LIST_FADE_MS = 250;

    private YelpApiClient mYelpApiClient;
    private CurrentLocationClient mCurrentLocationClient;
    private MapFragment mMapFragment;
    private ViewGroup mSplash;
    //private ViewGroup mTempItemPlaceHolder; //temp: to be replaced by the list

    private ViewPager mBusinessPager;
    private BusinessPagerAdapter mBusinessAdapter;


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
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mMapFragment = (MapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mMapFragment.setLocationListener(this);

        mSplash = (ViewGroup) findViewById(R.id.splash);
        TextView splashText = (TextView) findViewById(R.id.splash_text);
        MunchCustomizer.setSplashText(splashText);

        mBusinessPager = (ViewPager)findViewById(R.id.pager);
        mBusinessAdapter = new BusinessPagerAdapter(getSupportFragmentManager());
        mBusinessPager.setAdapter(mBusinessAdapter);


        mYelpApiClient.addListener(mBusinessAdapter);

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
        if (isSplashVisible()) {
            mMapFragment.animateIn(DURATION_MAP_SPLASH_CROSSFADE_MS);
            mSplash.animate().setDuration(DURATION_MAP_SPLASH_CROSSFADE_MS).alpha(0f);
        }
        mYelpApiClient.loadPlaces(LocationUtils.toBounds(latLng, SEARCH_RADIUS),
                MunchCustomizer.getSearchTerms(this));
    }

    private boolean isSplashVisible() {
        return mSplash.getAlpha() > 0 && mSplash.getVisibility() == View.VISIBLE;
    }


    @Override
    public void onBusinessesLoaded(List<Business> businesses) {
        mMapFragment.showBusinesses(businesses);
    }

    @Override
    public void onMapItemClicked(Business business) {
        showBusinessDetails(business);
    }

    @Override
    public void onMapClicked() {
       // mBusinessPager.animate().setDuration(DURATION_LIST_FADE_MS).alpha(0);
    }

    private void showBusinessDetails(Business business) {
        if (DEBUG) {
            Log.d(TAG, "showBusinessDetails");
        }
        int pos = mBusinessAdapter.getPositionForBusiness(business);
        mBusinessPager.setCurrentItem(pos, true);

        /*
        mBusinessPager.setVisibility(View.VISIBLE);
        if (mBusinessPager.getAlpha() < 1f) {
            mBusinessPager.animate().setDuration(DURATION_LIST_FADE_MS).alpha(1);
        } */
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (DEBUG) {
            Log.d(TAG, "onPageScrolled");
        }
        final Business business = mBusinessAdapter.getBusinessForPosition(position);
        mMapFragment.selectBusiness(business);
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

    public static class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return null; //PlaceholderFragment.newInstance(position + 1);
        }

            @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}

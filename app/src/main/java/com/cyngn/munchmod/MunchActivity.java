package com.cyngn.munchmod;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.cyngn.munchmod.data.CurrentLocationClient;
import com.cyngn.munchmod.data.YelpApiClient;
import com.cyngn.munchmod.utils.LocationUtils;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;
import com.yelp.clientlib.entities.Business;

import java.util.List;


public class MunchActivity extends FragmentActivity implements
        MapFragment.MapListener,
        YelpApiClient.ResultCallback
{
    private static final boolean DEBUG = false;
    private static final String TAG = "MunchActivity";

    private static final double SEARCH_RADIUS = 5000; //I think this is 5km?
    private static final long DURATION_MAP_SPLASH_CROSSFADE_MS = 450;
    private static final long DURATION_LIST_FADE_MS = 250;

    private YelpApiClient mYelpApiClient;
    private CurrentLocationClient mCurrentLocationClient;
    private MapFragment mMapFragment;
    private ListviewFragment mListFragment;
    private ViewGroup mSplash;
    private ViewGroup mTempItemPlaceHolder; //temp: to be replaced by the list

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

        mListFragment = (ListviewFragment) getSupportFragmentManager()
                .findFragmentById(R.id.listview);

        mSplash = (ViewGroup) findViewById(R.id.splash);

        mTempItemPlaceHolder = (ViewGroup) findViewById(R.id.item_placeholder);

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
            //TODO: set splash visibility to GONE
        }
        mYelpApiClient.loadPlaces(LocationUtils.toBounds(latLng, SEARCH_RADIUS), getSearchTerms());
    }

    private boolean isSplashVisible() {
        return mSplash.getAlpha() > 0 && mSplash.getVisibility() == View.VISIBLE;
    }

    // Get a comma delimited list of search terms
    private String getSearchTerms() {
        // TODO: concatenate the time of day
        final String terms[] = new String[] {
                "food",
                getTimeOfDayTerm(),
                getUserEntryTerm(),
        };
        return termsToString(terms);
    }

    private String getTimeOfDayTerm() {
        return "lunch";
    }

    private String getUserEntryTerm() {
        return null;
    }

    private static String termsToString(String[] terms) {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (String term : terms) {
            if (term == null || term.isEmpty()) {
                continue;
            }
            if (!isFirst) {
                builder.append(",");
            } else {
                isFirst = false;
            }
            builder.append(term);
        }
        return builder.toString();
    }

    @Override
    public void onBusinessesLoaded(List<Business> businesses) {
        mMapFragment.showBusinesses(businesses);
    }

    @Override
    public void onMapItemClicked(Business business) {
        //TODO: show the list instead
        // mListFragment.show();
        // mListFragment.scrollTo(itemId);
        showBusinessDetails(business);
    }

    @Override
    public void onMapClicked() {
        mTempItemPlaceHolder.animate().setDuration(DURATION_LIST_FADE_MS).alpha(0);
    }

    private void showBusinessDetails(Business business) {
        Log.d(TAG, "showBusinessDetails");
        ImageView iv = (ImageView) mTempItemPlaceHolder.findViewById(R.id.item_icon);
        Picasso.with(this).load(business.imageUrl()).into(iv);

        mTempItemPlaceHolder.setVisibility(View.VISIBLE);
        if (mTempItemPlaceHolder.getAlpha() < 1f) {
            mTempItemPlaceHolder.animate().setDuration(DURATION_LIST_FADE_MS).alpha(1);
        }
    }


}

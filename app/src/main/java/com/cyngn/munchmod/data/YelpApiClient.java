package com.cyngn.munchmod.data;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.cyngn.munchmod.R;
import com.google.android.gms.maps.model.LatLngBounds;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.BoundingBoxOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Yelp Businesses
 */
public class YelpApiClient {

    private static final String TAG = "YelpApiClient";

    private Context mContext;
    private YelpAPI mYelpApi;

    public YelpApiClient(Context context) {
        mContext = context;
        initYelpApi();
    }

    // Yelp API: loading the places
    private void initYelpApi() {
        final Resources res = mContext.getResources();
        final String consumerKey = res.getString(R.string.yelp_api_consumer_key);
        final String consumerSecret = res.getString(R.string.yelp_api_consumer_secret);
        final String token = res.getString(R.string.yelp_api_token);
        final String tokenSecret = res.getString(R.string.yelp_api_token_secret);

        final YelpAPIFactory apiFactory = new YelpAPIFactory(consumerKey, consumerSecret, token, tokenSecret);
        mYelpApi = apiFactory.createAPI();
    }

    public interface ResultCallback {
        void onBusinessesLoaded(List<Business> business);
    }

    private static class SearchCallback implements Callback<SearchResponse> {

        private List<Business> mBusinesses;
        private List<ResultCallback> mCallbackList = new ArrayList<>();


        public SearchCallback() {

        }

        @Override
        public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
            // response.message();
            Log.d(TAG, "SearchResponse: " + response.message());
            Log.d(TAG, "SearchResponse: " + response.body());

            mBusinesses = response.body().businesses();

            for (ResultCallback resultCallback : mCallbackList) {
                resultCallback.onBusinessesLoaded(mBusinesses);
            }
        }

        @Override
        public void onFailure(Call<SearchResponse> call, Throwable t) {
            Log.d(TAG, "SearchResponse: FAILURE" + t);
        }

        public List<Business> getBusinesses() {
            return mBusinesses;
        }

        public void addListener(ResultCallback resultCallback) {
            mCallbackList.add(resultCallback);
        }

        public void removeListener(ResultCallback resultCallback) {
            mCallbackList.remove(resultCallback);
        }
    };

    private SearchCallback mSearchCallback = new SearchCallback();


    private static final String SORT_DISTANCE = "1";
    private static final String SORT_HIGHEST_RATED = "2";
    /**
     * Reload places
     * @param latLngBounds
     */
    public void loadPlaces(LatLngBounds latLngBounds, String searchTerms) {

        BoundingBoxOptions bounds = BoundingBoxOptions.builder()
                .swLatitude(latLngBounds.southwest.latitude)
                .swLongitude(latLngBounds.southwest.longitude)
                .neLatitude(latLngBounds.northeast.latitude)
                .neLongitude(latLngBounds.northeast.longitude).build();

        Map<String, String> params = new HashMap<>();

        // general params
        params.put("term", searchTerms);
        params.put("limit", "6");
        params.put("actionlinks", "true");
        params.put("radius_filter", "5000");

        Call<SearchResponse> call = mYelpApi.search(bounds, params);
        call.enqueue(mSearchCallback);
    }


    /**
     *  Add a listener
     * @param resultCallback
     */
    public void addListener(ResultCallback resultCallback) {
        mSearchCallback.addListener(resultCallback);
    }

    /**
     * Remove a listener
     * @param resultCallback
     */
    public void removeListener(ResultCallback resultCallback) {
        mSearchCallback.removeListener(resultCallback);
    }


}

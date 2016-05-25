package com.cyngn.munchmod;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.graphics.Point;

import com.cyngn.munchmod.data.CurrentLocationClient;
import com.cyngn.munchmod.utils.LocationUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import com.yelp.clientlib.entities.Business;

import java.util.List;

/**
 * MapFragment: TODO: move all the google maps functionality
 * here
 */
public class MapFragment extends SupportMapFragment implements
        CurrentLocationClient.ResultCallback,
        OnMapReadyCallback,
        GoogleMap.OnCameraChangeListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMapClickListener {

    private static final String TAG = "MapFragment";
    private static final boolean DEBUG = true;

    // Constants
    private static final float MAP_DEFAULT_ZOOM_LEVEL = 16;
    private static final float MAP_INVALID_ZOOM_LEVEL = 0;

    private static final int MAP_CAMERA_ZOOM_DURATION_MS = 350;         //for zooming to a zoomlevel
    private static final int MAP_CAMERA_LATLNGBOUNDS_DURATION_MS = 300; //for zooming to a latlng bounds for radius
    private static final int MAP_CAMERA_LATLNG_ZOOM_DURATION_MS = 450;  //for zooming to a location


    /**
     * TODO: update the current location
     */
    private static final long MAX_CURRENT_LOCATION_AGE_MILLIS = 5000; //allow 5s before the old location is invalidated

    /**
     * Called when center of the map has changed
     */
    public interface MapLocationChangeListener {
        void onMapLocationChanged(LatLng latLng);
    }


    private GoogleMap mMap;
    private ViewGroup mContainer;
    private LatLng mLatLng;
    private Point mCenterPoint;
    private LatLngBounds mLatLngBounds;
    private float mPrevZoomLevel = MAP_INVALID_ZOOM_LEVEL;
    private float mZoomLevel = MAP_DEFAULT_ZOOM_LEVEL;
    private boolean mIsSelfCameraChange = false;

    private MapLocationChangeListener mLocationListener = null;

    private GoogleMap.CancelableCallback mCameraUpdateCallback =  new GoogleMap.CancelableCallback() {

        @Override
        public void onFinish() {

        }

        @Override
        public void onCancel() {
            mIsSelfCameraChange = false;
        }
    };



    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (DEBUG) {
            Log.d(TAG, "onCreate");
        }
        super.onCreate(savedInstanceState);
        startCurrentLocation();
    }

    /**
     * onCreateView: where the view should be initialized
     *
     * @param inflater
     * @param viewGroup
     * @param bundle
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        if (DEBUG) {
            Log.d(TAG, "onCreateView");
        }

        View mapView = super.onCreateView(inflater, viewGroup, bundle);
        mContainer = viewGroup; //new FrameLayout(getActivity());
       // mContainer.addView(mapView, new FrameLayout.LayoutParams(-1, -1));

        // TODO: add more controls?
        /*
        mContainer.addView(inflater.inflate(R.layout.one_map_controls, null), new FrameLayout.LayoutParams(-1, -1));
        mCircleView = (View) view.findViewById(R.id.location_circle);
        mPinLocationView = (ImageView) view.findViewById(R.id.location_pin_button);
        mCurrentLocationButton = (ImageButton)view.findViewById(R.id.location_current_button);
        */
        // Request the map
        getMapAsync(this);
        return mapView;
    }

    public void setLocationListener(MapLocationChangeListener listener) {
        mLocationListener = listener;
    }

    public int getMapWidth() {
        return mContainer.getWidth();
    }

    public int getMapHeight() {
        return mContainer.getHeight();
    }

    /**
     * LatLng of center pin
     * @return
     */
    public LatLng getCenterLatLng() {
        if (mCenterPoint == null) {
            mCenterPoint = new Point(mContainer.getWidth()/2, mContainer.getHeight()/2);
        }
        return mMap.getProjection().fromScreenLocation(mCenterPoint);
    }

    private void startCurrentLocation() {
        ((MunchApp) getActivity().getApplication()).getCurrentLocationClient().addListener(this);
    }

    private void stopCurrentLocation() {
        ((MunchApp) getActivity().getApplication()).getCurrentLocationClient().removeListener(this);
    }

    @Override
    public void onPause() {
        if (DEBUG) {
            Log.d(TAG, "onPause");
        }
        super.onPause();
        stopCurrentLocation();
    }

    @Override
    public void onResume() {
        if (DEBUG) {
            Log.d(TAG, "onResume");
        }
        super.onResume();
        startCurrentLocation();
    }

    @Override
    public void onCurrentLocation(Location location) {
        if (DEBUG) {
            Log.d(TAG, "onCurrentLocation");
        }
        stopCurrentLocation();
        updateLocation(new LatLng(location.getLatitude(), location.getLongitude()), true);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (DEBUG) {
            Log.d(TAG, "onMapReady");
        }
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnCameraChangeListener(this);
        mMap.setOnMapClickListener(this);
        try {
            mMap.setMyLocationEnabled(true);
        }
        catch (SecurityException se) {
            Log.d(TAG, "SecurityException for mMap.setMyLocationEnabled");
        }
        final UiSettings settings = mMap.getUiSettings();
        settings.setMyLocationButtonEnabled(true);
        settings.setZoomControlsEnabled(true);
        settings.setRotateGesturesEnabled(false);
        settings.setTiltGesturesEnabled(false);

        if (mLatLng != null) {
            // force a visual location update
            showLocation();
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

        final boolean zoomLevelChanged = (mPrevZoomLevel != cameraPosition.zoom);
        if (DEBUG) {
            Log.d(TAG, "onCameraChange: " + cameraPosition + " prevZoom:" +
                    mPrevZoomLevel + " zoomLevelChanged=" + zoomLevelChanged);
        }

        mPrevZoomLevel = cameraPosition.zoom;

        // Our own camera change
        if (mIsSelfCameraChange) {
            mIsSelfCameraChange = false;
            return;
        }

        // TODO: user is changing map location, kick off another
        // User has moved pin to new location
        final LatLng latLng = getCenterLatLng();
        if (LocationUtils.isNegligibleLocationChange(latLng, mLatLng)) {
            return;
        }
        if (DEBUG) {
            Log.d(TAG, "onCameraChange found new latLng: " + latLng);
        }

        updateLocation(latLng, false);
    }


    @Override
    public void onMapClick(LatLng latLng) {
        //TODO:
        if (DEBUG) {
            Log.d(TAG, "onMapClick latLng=" + latLng);
        }
    }

    /**
     * onMyLocationButtonClick button pressed
     */
     @Override
     public boolean onMyLocationButtonClick() {
         if (DEBUG) {
            Log.d(TAG, "onMyLocationButtonClick ");
         }

         // Zoom to default level if we're too zoomed out
         if (mPrevZoomLevel < MAP_DEFAULT_ZOOM_LEVEL) {
             mZoomLevel = MAP_DEFAULT_ZOOM_LEVEL;
         }

         startCurrentLocation();
         return false;
     }


    /**
     * show a list of businesses
     * @param businesses
     */
    public void showBusinesses(List<Business> businesses) {
        Log.d(TAG, "onBusinessesLoaded received " + businesses.size() + " businesses");
        for (Business business : businesses) {
            //TODO: show these in list view and map view
            Log.d(TAG, "Business: " + business.name());
        }
    }


    private void updateLocation(LatLng newLocation, boolean mapUpdateNeeded) {
        mLatLng = newLocation;
        if (mapUpdateNeeded) {
            showLocation();
        }
        if (mLocationListener != null) {
            mLocationListener.onMapLocationChanged(mLatLng);
        }
    }

    /**
     * Set up Marker for a location and move camera there
     * Requires valid map
     */

    private void showLocation() {

        if (mMap == null) {
            Log.d(TAG, "showLocation called when map is still NULL!");
            return;
        }
        // Radius Bounds
        if (mLatLngBounds != null) {
            if (DEBUG) {
                Log.d(TAG, "showLocation animating to latLngBounds:" + mLatLngBounds);
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mLatLngBounds,
                  getMapWidth(),
                  getMapHeight(), 0), MAP_CAMERA_LATLNGBOUNDS_DURATION_MS, mCameraUpdateCallback);

            mLatLngBounds = null;
            return;
        }

        // Which is the location we should show?
        final LatLng latLng = mLatLng;
        // Zooming to a level
        if (mZoomLevel != MAP_INVALID_ZOOM_LEVEL) {

                if (DEBUG) {
                    Log.d(TAG, "showLocation zooming to:" + mZoomLevel + " center:" + latLng);
                }
                if (latLng != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, mZoomLevel),
                            MAP_CAMERA_LATLNG_ZOOM_DURATION_MS,
                            mCameraUpdateCallback);
                } else {
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(mZoomLevel),
                            MAP_CAMERA_ZOOM_DURATION_MS,
                            mCameraUpdateCallback);
                }
                mZoomLevel = 0;
                return;
        }

        // No zoom change just go to location
        if (latLng != null) {

            // Only animate if location is different
            if (!latLng.equals(mMap.getCameraPosition().target)) {
                // Setting center
                if (DEBUG) {
                    Log.d(TAG, "showLocation centering to:" + latLng);
                }
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, mMap.getCameraPosition().zoom),
                        MAP_CAMERA_LATLNG_ZOOM_DURATION_MS,
                        mCameraUpdateCallback);
            }
        }

    }

}


package com.cyngn.munchmod.utils;

import android.content.Context;
import android.location.Location;

import com.cyngn.munchmod.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;
import com.yelp.clientlib.entities.Coordinate;

/**
 * LocationUtils:
 *
 */
public class LocationUtils {

    private LocationUtils() {}

    private static final double MAP_MAX_NEGLIGIBLE_OFFSET = 10f; //10 meters
    private static final double MAX_WALK_DISTANCE = 2000f; //2km
    private static final double SPEED_WALKING_METERS_PER_MINUTE = 83; //83 meters
    private static final double SPEED_DRIVING_METERS_PER_MINUTE = 500;//500 meters

    public static LatLngBounds toBounds(LatLng center, double radius) {
        LatLng southwest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 225);
        LatLng northeast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 45);
        return new LatLngBounds(southwest, northeast);
    }


    /**
     * Return whether 2 different lat/lng locations are close enough
     * to be regarded as the same
     * (reduces thrashing on Address geocode lookups)
     * @param latLng1
     * @param latLng2
     * @return
     */
    public static boolean isNegligibleLocationChange(LatLng latLng1, LatLng latLng2) {
        if (latLng1 == latLng2) {
            return true;
        }
        if (latLng1 == null || latLng2 == null) {
            return false;
        }
        return (SphericalUtil.computeDistanceBetween(latLng1, latLng2) <
                MAP_MAX_NEGLIGIBLE_OFFSET);

    }

    public static double computeDistance(LatLng latLng1, LatLng latLng2) {
        return SphericalUtil.computeDistanceBetween(latLng1, latLng2);
    }

    /**
     * Get the "10 mins walk" distance time text
     * for the mapped location link
     * @param latLng1
     * @param latLng2
     * @return
     */
    public static String formatDistanceTimeText(Context context, LatLng latLng1, LatLng latLng2) {
        double distance = computeDistance(latLng1, latLng2);
        // driving minutes
        if (distance > MAX_WALK_DISTANCE) {
            return context.getString(R.string.distance_drive_mins,
                    (int)(distance/SPEED_DRIVING_METERS_PER_MINUTE));
        }
        // walking minutes
        return context.getString(R.string.distance_walk_mins,
                (int)(distance/SPEED_WALKING_METERS_PER_MINUTE));

    }

    public static LatLng coordinateToLatLng(Coordinate coordinate) {
        return new LatLng(
                coordinate.latitude(),
                coordinate.longitude()
        );
    }

    public static LatLng locationToLatLng(Location location) {
        return new LatLng(
                location.getLatitude(),
                location.getLongitude()
        );
    }
}

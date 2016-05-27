package com.cyngn.munchmod.utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;

/**
 * LocationUtils:
 *
 */
public class LocationUtils {

    private LocationUtils() {}

    private static final double MAP_MAX_NEGLIGIBLE_OFFSET = 10f; //10 meters

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

    public static String computeDistanceTimeText(LatLng latLng1, LatLng latLng2) {
        return "10 mins walk";
    }
}

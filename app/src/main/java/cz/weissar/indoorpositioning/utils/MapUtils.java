package cz.weissar.indoorpositioning.utils;

import android.location.Location;

/**
 * Created by petrw on 18.01.2018.
 */

public class MapUtils {

    public static class LatLng {

        double lat;
        double lng;

        public LatLng(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }
    }

    public static LatLng distanceInGps(LatLng oldPos, double metersLat, double metersLng) {  // generally used geo measurement function
        int r = 6_371_000; //meters
        double newLat = oldPos.getLat() + (metersLat / r) * (180 / Math.PI);
        double newLng = oldPos.getLng() + (metersLng / r) * (180 / Math.PI) / Math.cos(oldPos.getLat() * Math.PI / 180);
        return new LatLng(newLat, newLng);
    }

    public static float distanceInMeters(Location a, double bLat, double bLon) {
        return distanceInMeters(a.getLatitude(), a.getLongitude(), bLat, bLon);
    }

    public static float distanceInMeters(double latA, double lngA, double latB, double lngB) {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(latB - latA);
        double lngDiff = Math.toRadians(lngB - lngA);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                Math.cos(Math.toRadians(latA)) * Math.cos(Math.toRadians(latB)) *
                        Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return (float) (distance * meterConversion);
    }
}

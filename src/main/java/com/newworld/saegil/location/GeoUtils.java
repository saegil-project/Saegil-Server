package com.newworld.saegil.location;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GeoUtils {

    private static final int EARTH_RADIUS_METERS = 6371000;

    public static double calculateDistanceMeters(
            final Coordinates coordinates1,
            final Coordinates coordinates2
    ) {
        final double radLatitude1 = Math.toRadians(coordinates1.latitude());
        final double radLatitude2 = Math.toRadians(coordinates2.latitude());
        final double radLongitude1 = Math.toRadians(coordinates1.longitude());
        final double radLongitude2 = Math.toRadians(coordinates2.longitude());

        final double dLat = radLatitude2 - radLatitude1;
        final double dLon = radLongitude2 - radLongitude1;

        final double a = sin2(dLat / 2) + Math.cos(radLatitude1) * Math.cos(radLatitude2) * sin2(dLon / 2);

        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_METERS * c;
    }

    private static double sin2(double x) {
        final double sinX = Math.sin(x);
        return sinX * sinX;
    }

    public static GeoBoundingBox calculateBoundingBox(double latitude, double longitude, double radiusMeters) {
        final double radLatitude = Math.toRadians(latitude);

        final double angularRadius = radiusMeters / EARTH_RADIUS_METERS;

        final double dLat = Math.toDegrees(angularRadius);
        final double dLon = Math.toDegrees(angularRadius / Math.cos(radLatitude));
        double minLatitude = latitude - dLat;
        double maxLat = latitude + dLat;

        double minLon = longitude - dLon;
        double maxLon = longitude + dLon;

        return new GeoBoundingBox(minLatitude, maxLat, minLon, maxLon);
    }
}

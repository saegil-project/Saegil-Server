package com.newworld.saegil.location;

public record GeoBoundingBox(
        double minLatitude,
        double maxLatitude,
        double minLongitude,
        double maxLongitude
) {
}

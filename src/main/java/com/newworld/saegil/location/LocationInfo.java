package com.newworld.saegil.location;

public record LocationInfo(
        String roadAddress,
        String jibunAddress,
        GeoPoint geoPoint
) {

    public Double getLatitude() {
        return geoPoint.latitude();
    }

    public Double getLongitude() {
        return geoPoint.longitude();
    }
}

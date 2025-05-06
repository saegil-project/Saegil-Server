package com.newworld.saegil.location;

public record LocationInfo(
        String roadAddress,
        String jibunAddress,
        Coordinates coordinates
) {

    public Double getLatitude() {
        return coordinates.latitude();
    }

    public Double getLongitude() {
        return coordinates.longitude();
    }
}

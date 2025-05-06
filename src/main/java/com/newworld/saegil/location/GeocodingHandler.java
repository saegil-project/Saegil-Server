package com.newworld.saegil.location;

public interface GeocodingHandler {

    LocationInfo getLocationInfo(String address) throws GeocodingException;
}

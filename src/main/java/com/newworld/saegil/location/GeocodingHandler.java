package com.newworld.saegil.location;

public interface GeocodingHandler {

    LocationInfo getAddress(String address) throws GeocodingException;
}

package com.newworld.saegil.location;

public interface GeocodingHandler {

    Coordinates getCoordinates(String address) throws GeocodingException;
}

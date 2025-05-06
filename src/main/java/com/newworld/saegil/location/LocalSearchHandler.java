package com.newworld.saegil.location;

public interface LocalSearchHandler {

    LocationInfo getLocationInfo(String placeName) throws LocalSearchException;
}

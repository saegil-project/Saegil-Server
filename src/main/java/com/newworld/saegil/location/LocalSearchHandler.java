package com.newworld.saegil.location;

public interface LocalSearchHandler {

    LocationInfo getAddress(String placeName) throws LocalSearchException;
}

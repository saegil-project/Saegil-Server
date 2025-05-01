package com.newworld.saegil.location;

public interface LocalSearchHandler {

    Coordinates getCoordinates(String placeName) throws LocationSearchException;
}

package com.newworld.saegil.location;

public interface LocalSearchHandler {

    Address getAddress(String placeName) throws LocalSearchException;
}

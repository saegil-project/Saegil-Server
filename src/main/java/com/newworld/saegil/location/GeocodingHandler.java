package com.newworld.saegil.location;

public interface GeocodingHandler {

    Address getAddress(String address) throws GeocodingException;
}

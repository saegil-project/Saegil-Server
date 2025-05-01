package com.newworld.saegil.location;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Address {

    private final String roadAddress;
    private final String jibunAddress;
    private final Coordinates coordinates;
}

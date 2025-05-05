package com.newworld.saegil.location;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class LocationInfoResolver {

    private final GeocodingHandler geocodingHandler;
    private final LocalSearchHandler localSearchHandler;

    public LocationInfo resolve(String address, String placeName) throws LocationInfoResolveFailedException {
        if (StringUtils.hasText(address)) {
            return resolveByAddressOrElsePlaceName(address, placeName);
        }

        if (StringUtils.hasText(placeName)) {
            return resolveByPlaceName(placeName);
        }

        throw new LocationInfoResolveFailedException("주소와 장소명 모두 없음");
    }

    private LocationInfo resolveByAddressOrElsePlaceName(
            final String address,
            final String placeName
    ) throws LocationInfoResolveFailedException {
        try {
            return geocodingHandler.getAddress(address);
        } catch (GeocodingException e) {
            if (StringUtils.hasText(placeName)) {
                return resolveByPlaceName(placeName);
            }

            throw new LocationInfoResolveFailedException("주소(" + address + ")의 좌표를 찾을 수 없습니다: " + e.getMessage());
        }
    }

    private LocationInfo resolveByPlaceName(final String placeName) throws LocationInfoResolveFailedException {
        try {
            return localSearchHandler.getAddress(placeName);
        } catch (LocalSearchException e) {
            throw new LocationInfoResolveFailedException(
                    "장소(" + placeName + ")의 좌표를 찾을 수 없습니다: " + e.getMessage()
            );
        }
    }
}

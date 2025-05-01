package com.newworld.saegil.location;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
@Slf4j
public class AddressResolver {

    private final GeocodingHandler geocodingHandler;
    private final LocalSearchHandler localSearchHandler;

    public Address resolve(String address, String placeName) {
        if (StringUtils.hasText(address)) {
            return resolveByAddressOrElsePlaceName(address, placeName);
        }

        if (StringUtils.hasText(placeName)) {
            return resolveByPlaceName(placeName);
        }

        log.error("주소와 장소명 모두 없음");
        throw new AddressResolveFailedException("주소와 장소명 모두 없음");
    }

    private Address resolveByAddressOrElsePlaceName(final String address, final String placeName) {
        try {
            return geocodingHandler.getAddress(address);
        } catch (GeocodingException e) {
            log.error("주소({})의 좌표를 찾을 수 없습니다: {}", address, e.getMessage());

            if (StringUtils.hasText(placeName)) {
                return resolveByPlaceName(placeName);
            }

            throw new AddressResolveFailedException("주소(" + address + ")의 좌표를 찾을 수 없습니다: " + e.getMessage());
        }
    }

    private Address resolveByPlaceName(final String placeName) {
        try {
            return localSearchHandler.getAddress(placeName);
        } catch (LocationSearchException e) {
            log.error("장소({})의 좌표를 찾을 수 없습니다: {}", placeName, e.getMessage());

            throw new AddressResolveFailedException("장소(" + placeName + ")의 좌표를 찾을 수 없습니다: " + e.getMessage());
        }
    }
}

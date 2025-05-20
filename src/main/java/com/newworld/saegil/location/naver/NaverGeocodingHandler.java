package com.newworld.saegil.location.naver;

import com.newworld.saegil.location.Coordinates;
import com.newworld.saegil.location.GeocodingException;
import com.newworld.saegil.location.GeocodingHandler;
import com.newworld.saegil.location.LocationInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NaverGeocodingHandler implements GeocodingHandler {

    private final NaverGeocodingProperties properties;
    private final RestTemplate restTemplate;

    @Override
    public LocationInfo getLocationInfo(String address) throws GeocodingException {
        final String requestUri = UriComponentsBuilder.fromUriString(properties.apiUri())
                                                      .queryParam("query", address)
                                                      .build(false)
                                                      .toUriString();

        final HttpEntity<Void> requestEntity = createRequestEntity();

        final ResponseEntity<NaverGeocodingResponse> responseBody = restTemplate.exchange(
                requestUri,
                HttpMethod.GET,
                requestEntity,
                NaverGeocodingResponse.class
        );

        if (!responseBody.getStatusCode().is2xxSuccessful()) {
            throw new GeocodingException("Naver Geocoding 요청에 실패했습니다. (주소: " + address + ")");
        }

        final AddressResponse firstAddress = extractFirstAddress(responseBody, address);

        return firstAddress.toLocationInfo();
    }

    private HttpEntity<Void> createRequestEntity() {
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("x-ncp-apigw-api-key-id", properties.clientId());
        requestHeaders.set("x-ncp-apigw-api-key", properties.clientSecret());

        return new HttpEntity<>(requestHeaders);
    }

    private AddressResponse extractFirstAddress(
            final ResponseEntity<NaverGeocodingResponse> responseBody,
            final String address
    ) throws GeocodingException {
        final NaverGeocodingResponse response = responseBody.getBody();
        if (response == null || response.addresses == null || response.addresses.isEmpty()) {
            throw new GeocodingException("Naver Geocoding에서 해당 장소를 찾을 수 없습니다. (주소: " + address + ")");
        }

        return response.addresses.getFirst();
    }

    record NaverGeocodingResponse (
        String status,
        String errorMessage,
        List<AddressResponse> addresses
    ) {
    }

    record AddressResponse (
        String roadAddress,
        String jibunAddress,
        String englishAddress,
        String x,
        String y
    ) {

        public LocationInfo toLocationInfo() throws GeocodingException {
            if (x == null || y == null) {
                throw new GeocodingException("Naver Geocoding 응답에 좌표 정보가 없습니다.");
            }

            final double latitude = Double.parseDouble(y);
            final double longitude = Double.parseDouble(x);
            final Coordinates coordinates = new Coordinates(latitude, longitude);

            return new LocationInfo(roadAddress, jibunAddress, coordinates);
        }
    }
}

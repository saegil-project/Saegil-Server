package com.newworld.saegil.location.naver;

import com.newworld.saegil.location.Address;
import com.newworld.saegil.location.Coordinates;
import com.newworld.saegil.location.LocationSearchException;
import com.newworld.saegil.location.LocalSearchHandler;
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
public class NaverLocalSearchHandler implements LocalSearchHandler {

    private final NaverLocationSearchProperties properties;
    private final RestTemplate restTemplate;

    @Override
    public Address getAddress(String placeName) throws LocationSearchException {
        final String requestUri = UriComponentsBuilder.fromUriString(properties.apiUri())
                                                      .queryParam("query", placeName)
                                                      .queryParam("display", 1)
                                                      .build(false)
                                                      .toUriString();

        final HttpEntity<Void> requestEntity = createRequestEntity();

        final ResponseEntity<NaverLocationSearchResponse> responseBody = restTemplate.exchange(
                requestUri,
                HttpMethod.GET,
                requestEntity,
                NaverLocationSearchResponse.class
        );

        if (!responseBody.getStatusCode().is2xxSuccessful()) {
            throw new LocationSearchException("Naver Loocation Search 요청에 실패했습니다.");
        }

        final NaverLocationSearchItem firstItem = extractFirstItem(responseBody);

        return firstItem.toAddress();
    }

    private HttpEntity<Void> createRequestEntity() {
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("X-Naver-Client-Id", properties.clientId());
        requestHeaders.set("X-Naver-Client-Secret", properties.clientSecret());

        return new HttpEntity<>(requestHeaders);
    }

    private NaverLocationSearchItem extractFirstItem(
            final ResponseEntity<NaverLocationSearchResponse> responseBody
    ) throws LocationSearchException {
        final NaverLocationSearchResponse response = responseBody.getBody();
        if (response == null || response.items == null || response.items.isEmpty()) {
            throw new LocationSearchException("Naver Location Search에서 해당 장소를 찾을 수 없습니다.");
        }

        return response.items.getFirst();
    }

    record NaverLocationSearchResponse(
            List<NaverLocationSearchItem> items
    ) {
    }

    record NaverLocationSearchItem(
            String title,
            String address,
            String roadAddress,
            String mapx,
            String mapy
    ) {

        public Address toAddress() throws LocationSearchException {
            if (mapx == null || mapy == null) {
                throw new LocationSearchException("Naver Location Search 응답에 좌표 정보가 없습니다.");
            }

            final double latitude = Double.parseDouble(mapy) / 10_000_000.0;
            final double longitude = Double.parseDouble(mapx) / 10_000_000.0;
            final Coordinates coordinates = new Coordinates(latitude, longitude);

            return new Address(roadAddress, address, coordinates);
        }
    }
}

package com.newworld.saegil.location.naver;

import com.newworld.saegil.location.GeoPoint;
import com.newworld.saegil.location.LocalSearchException;
import com.newworld.saegil.location.LocalSearchHandler;
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
public class NaverLocalSearchHandler implements LocalSearchHandler {

    private final NaverLocationSearchProperties properties;
    private final RestTemplate restTemplate;

    @Override
    public LocationInfo getLocationInfo(String placeName) throws LocalSearchException {
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
            throw new LocalSearchException("Naver Loocation Search 요청에 실패했습니다.");
        }

        final NaverLocationSearchItem firstItem = extractFirstItem(responseBody);

        return firstItem.toLoginInfo();
    }

    private HttpEntity<Void> createRequestEntity() {
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("X-Naver-Client-Id", properties.clientId());
        requestHeaders.set("X-Naver-Client-Secret", properties.clientSecret());

        return new HttpEntity<>(requestHeaders);
    }

    private NaverLocationSearchItem extractFirstItem(
            final ResponseEntity<NaverLocationSearchResponse> responseBody
    ) throws LocalSearchException {
        final NaverLocationSearchResponse response = responseBody.getBody();
        if (response == null || response.items == null || response.items.isEmpty()) {
            throw new LocalSearchException("Naver Location Search에서 해당 장소를 찾을 수 없습니다.");
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

        public LocationInfo toLoginInfo() throws LocalSearchException {
            if (mapx == null || mapy == null) {
                throw new LocalSearchException("Naver Location Search 응답에 좌표 정보가 없습니다.");
            }

            final double latitude = Double.parseDouble(mapy) / 10_000_000.0;
            final double longitude = Double.parseDouble(mapx) / 10_000_000.0;
            final GeoPoint geoPoint = new GeoPoint(latitude, longitude);

            return new LocationInfo(roadAddress, address, geoPoint);
        }
    }
}

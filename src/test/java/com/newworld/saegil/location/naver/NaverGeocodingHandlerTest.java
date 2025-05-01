package com.newworld.saegil.location.naver;

import com.newworld.saegil.location.Coordinates;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled
class NaverGeocodingHandlerTest {

    @Autowired
    private NaverGeocodingHandler naverGeocodingHandler;

    @Test
    void testGetCoordinates() throws Exception {
        final String address = "서울 동작구 사당로 50";
        final Coordinates actual = naverGeocodingHandler.getCoordinates(address);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isNotNull();
            softAssertions.assertThat(actual.getLatitude()).isEqualTo(37.4944897);
            softAssertions.assertThat(actual.getLongitude()).isEqualTo(126.9597657);
        });
    }
}

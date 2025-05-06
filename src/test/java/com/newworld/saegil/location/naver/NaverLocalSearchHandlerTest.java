package com.newworld.saegil.location.naver;

import com.newworld.saegil.location.LocationInfo;
import com.newworld.saegil.location.Coordinates;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Disabled
class NaverLocalSearchHandlerTest {

    @Autowired
    private NaverLocalSearchHandler naverLocationSearchHandler;

    @Test
    void testGetAddress() throws Exception {
        // given
        final String placeName = "숭실대학교 정보과학관";
        final LocationInfo expected = new LocationInfo(
                "서울특별시 동작구 사당로 50",
                "서울특별시 동작구 상도동 509",
                new Coordinates(37.4944897, 126.9597657)
        );

        // when
        final LocationInfo actual = naverLocationSearchHandler.getLocationInfo(placeName);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}

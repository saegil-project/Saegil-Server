package com.newworld.saegil.location.naver;

import com.newworld.saegil.location.Address;
import com.newworld.saegil.location.Coordinates;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Disabled
class NaverGeocodingHandlerTest {

    @Autowired
    private NaverGeocodingHandler naverGeocodingHandler;

    @Test
    void testGetAddress() throws Exception {
        // given
        final String address = "서울 동작구 사당로 50";
        final Address expected = new Address(
                "서울특별시 동작구 사당로 50 숭실대학교 정보과학관",
                "서울특별시 동작구 상도동 509 숭실대학교 정보과학관",
                new Coordinates(37.4944897, 126.9597657)
        );

        // when
        final Address actual = naverGeocodingHandler.getAddress(address);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}

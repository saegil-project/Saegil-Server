package com.newworld.saegil.location.naver;

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
    void testGetCoordinates() throws Exception {
        final String address = "숭실대학교 정보과학관";
        final Coordinates actual = naverLocationSearchHandler.getCoordinates(address);

        assertThat(actual).isNotNull();
        assertThat(actual.getLatitude()).isEqualTo(37.4944897);
        assertThat(actual.getLongitude()).isEqualTo(126.9597657);
    }
}

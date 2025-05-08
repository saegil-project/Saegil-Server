package com.newworld.saegil.location;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class GeoUtilsTest {

    @Nested
    @DisplayName("두 위/경도 좌표 사이의 거리 계산")
    class CalculateDistanceMeters {

        @Nested
        @DisplayName("서로 다른 위치라면")
        class DifferentLocations {

            @Test
            void 오차범위가_1퍼센트_미만이다() {
                // given
                final Coordinates seoul = new Coordinates(37.5665, 126.9780);  // 서울
                final Coordinates busan = new Coordinates(35.1796, 129.0756);  // 부산

                final double expect = 325000; // 325km

                // when
                double actual = GeoUtils.calculateDistanceMeters(seoul, busan);

                // then
                final double tolerance = 0.01;  // 허용 오차 1%
                assertThat(actual).isBetween(expect * (1 - tolerance), expect * (1 + tolerance));
            }
        }

        @Nested
        @DisplayName("같은 위치라면")
        class SameLocation {

            @Test
            void 거리는_0이다() {
                // given
                final Coordinates seoul = new Coordinates(37.5665, 126.9780);  // 서울

                final double expect = 0;

                // when
                final double actual = GeoUtils.calculateDistanceMeters(seoul, seoul);

                // then
                assertThat(actual).isEqualTo(expect);
            }
        }
    }
}


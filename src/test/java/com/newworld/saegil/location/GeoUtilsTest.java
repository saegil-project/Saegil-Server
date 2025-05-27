package com.newworld.saegil.location;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.within;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class GeoUtilsTest {

    @Nested
    @DisplayName("두 위/경도 좌표 사이의 거리 계산")
    class Describe_calculateDistanceMeters {

        @Nested
        @DisplayName("서로 다른 위치라면")
        class Context_different_locations {

            @Test
            void 오차범위가_1퍼센트_미만이다() {
                // given
                final GeoPoint seoul = new GeoPoint(37.5665, 126.9780);  // 서울
                final GeoPoint busan = new GeoPoint(35.1796, 129.0756);  // 부산

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
        class Context_same_location {

            @Test
            void 거리는_0이다() {
                // given
                final GeoPoint seoul = new GeoPoint(37.5665, 126.9780);  // 서울

                final double expect = 0;

                // when
                final double actual = GeoUtils.calculateDistanceMeters(seoul, seoul);

                // then
                assertThat(actual).isEqualTo(expect);
            }
        }
    }

    @Nested
    @DisplayName("중심 위치 좌표와 반지름으로 위/경도 BoundingBox 계산")
    class Describe_calculateBoundingBox {

        @Nested
        @DisplayName("위도와 경도, 반지름이 주어지면")
        class Context_with_geo_point_and_radius {

            @Test
            void BoundingBox를_계산한다() {
                // given
                final double centerLatitude = 37.5665; // 서울
                final double centerLongitude = 126.9780; // 서울
                final GeoPoint center = new GeoPoint(centerLatitude, centerLongitude); // 서울
                double radius = 1000; // 1km

                final double tolerance = 0.01; // 허용 오차 1%

                // when
                GeoBoundingBox box = GeoUtils.calculateBoundingBox(center.latitude(), center.longitude(), radius);

                // then
                final GeoPoint east = new GeoPoint(center.latitude(), box.maxLongitude());
                final GeoPoint west = new GeoPoint(center.latitude(), box.minLongitude());
                final GeoPoint south = new GeoPoint(box.minLatitude(), center.longitude());
                final GeoPoint north = new GeoPoint(box.maxLatitude(), center.longitude());

                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(GeoUtils.calculateDistanceMeters(center, east)).isCloseTo(radius, within(tolerance));
                    softAssertions.assertThat(GeoUtils.calculateDistanceMeters(center, west)).isCloseTo(radius, within(tolerance));
                    softAssertions.assertThat(GeoUtils.calculateDistanceMeters(center, south)).isCloseTo(radius, within(tolerance));
                    softAssertions.assertThat(GeoUtils.calculateDistanceMeters(center, north)).isCloseTo(radius, within(tolerance));
                });
            }
        }
    }
}


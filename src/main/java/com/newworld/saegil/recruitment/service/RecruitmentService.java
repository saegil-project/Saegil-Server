package com.newworld.saegil.recruitment.service;

import com.newworld.saegil.location.GeoBoundingBox;
import com.newworld.saegil.location.GeoPoint;
import com.newworld.saegil.location.GeoUtils;
import com.newworld.saegil.recruitment.domain.Recruitment;
import com.newworld.saegil.recruitment.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RecruitmentService {

    private final RecruitmentRepository recruitmentRepository;

    public List<NearbyRecruitmentDto> readNearbyRecruitments(
            final GeoPoint baseGeoPoint,
            final int radius,
            final LocalDateTime targetDateTime
    ) {
        final GeoBoundingBox geoBoundingBox = GeoUtils.calculateBoundingBox(
                baseGeoPoint.latitude(),
                baseGeoPoint.longitude(),
                radius
        );
        final List<Recruitment> recruitmentsInBoundingBox =
                recruitmentRepository.findAllActiveInBoundingBox(geoBoundingBox, targetDateTime);

        return recruitmentsInBoundingBox.stream()
                                        .map(recruitment -> NearbyRecruitmentDto.from(
                                                recruitment,
                                                GeoUtils.calculateDistanceMeters(baseGeoPoint, recruitment.getGeoPoint())
                                        )).filter(recruitmentDto -> recruitmentDto.distanceMeters() <= radius)
                                        .toList();
    }
}

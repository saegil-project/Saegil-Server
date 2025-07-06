package com.newworld.saegil.recruitment.repository;

import com.newworld.saegil.location.GeoBoundingBox;
import com.newworld.saegil.recruitment.domain.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {

    @Query("""
            SELECT r
            FROM Recruitment r
            WHERE NOT (r.recruitmentEndDate < :targetDateTime)
              AND r.latitude BETWEEN :#{#geoBoundingBox.minLatitude} AND :#{#geoBoundingBox.maxLatitude}
              AND r.longitude BETWEEN :#{#geoBoundingBox.minLongitude} AND :#{#geoBoundingBox.maxLongitude}
            """)
    List<Recruitment> findAllActiveInBoundingBox(final GeoBoundingBox geoBoundingBox, final LocalDateTime targetDateTime);
}

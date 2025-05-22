package com.newworld.saegil.recruitment.repository;

import com.newworld.saegil.recruitment.domain.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {
}

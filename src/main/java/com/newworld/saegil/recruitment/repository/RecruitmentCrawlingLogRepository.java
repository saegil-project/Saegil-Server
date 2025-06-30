package com.newworld.saegil.recruitment.repository;

import com.newworld.saegil.recruitment.domain.RecruitmentCrawlingLog;
import com.newworld.saegil.recruitment.domain.RecruitmentInfoSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface RecruitmentCrawlingLogRepository extends JpaRepository<RecruitmentCrawlingLog, Long> {

    boolean existsByInfoSourceAndCrawlingDate(final RecruitmentInfoSource infoSource, final LocalDate crawlingDate);
}

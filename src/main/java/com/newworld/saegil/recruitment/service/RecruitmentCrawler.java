package com.newworld.saegil.recruitment.service;

import com.newworld.saegil.recruitment.domain.Recruitment;
import com.newworld.saegil.recruitment.domain.RecruitmentInfoSource;

import java.time.LocalDate;
import java.util.List;

public interface RecruitmentCrawler {

    RecruitmentInfoSource getSupportingRecruitmentInfoSource();

    List<Recruitment> crawl(final LocalDate requestDate);
}

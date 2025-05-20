package com.newworld.saegil.recruitment.service;

import com.newworld.saegil.recruitment.domain.Recruitment;

import java.time.LocalDate;
import java.util.List;

public interface RecruitmentCrawler {

    List<Recruitment> crawl(final LocalDate requestDate);
}

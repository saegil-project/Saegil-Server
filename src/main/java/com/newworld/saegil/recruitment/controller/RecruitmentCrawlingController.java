package com.newworld.saegil.recruitment.controller;

import com.newworld.saegil.recruitment.service.RecruitmentCrawlingService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recruitments")
@Hidden
public class RecruitmentCrawlingController {

    private final RecruitmentCrawlingService recruitmentCrawlingService;

    @PostMapping("/crawling")
    public ResponseEntity<Void> crawlingFacilities(
            @RequestParam final LocalDate requestDate
    ) {
        recruitmentCrawlingService.fetchRecruitments(requestDate);

        return ResponseEntity.ok().build();
    }
}

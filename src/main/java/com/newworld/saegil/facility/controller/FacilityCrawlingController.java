package com.newworld.saegil.facility.controller;

import com.newworld.saegil.facility.service.FacilityCrawlingService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/facilities")
@Hidden
public class FacilityCrawlingController {

    private final FacilityCrawlingService facilityCrawlingService;

    @PostMapping("/crawling")
    public ResponseEntity<Void> crawlingFacilities() {
        facilityCrawlingService.fetchAll();

        return ResponseEntity.ok().build();
    }
}

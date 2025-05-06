package com.newworld.saegil.facility.service;

import com.newworld.saegil.facility.domain.Facility;
import com.newworld.saegil.facility.domain.FacilityInfoSource;

import java.util.List;

public interface FacilityCrawler {

    FacilityInfoSource getSupportingFacilityInfoSource();

    List<Facility> crawl();
}

package com.newworld.saegil.organization.service;

import com.newworld.saegil.organization.domain.Facility;
import com.newworld.saegil.organization.domain.FacilityInfoSource;

import java.util.List;

public interface FacilityCrawler {

    FacilityInfoSource getSupportingFacilityInfoSource();

    List<Facility> crawl();
}

package com.newworld.saegil.facility.service;

import com.newworld.saegil.location.LocationInfo;
import com.newworld.saegil.location.LocationInfoResolveFailedException;
import com.newworld.saegil.location.LocationInfoResolver;
import com.newworld.saegil.facility.domain.Facility;
import com.newworld.saegil.facility.domain.FacilityInfoSource;
import com.newworld.saegil.facility.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FacilityCrawlingService {

    private final Set<FacilityCrawler> crawlers;
    private final LocationInfoResolver locationInfoResolver;
    private final FacilityRepository facilityRepository;

    public void fetchAll() {
        for (final FacilityCrawler crawler : crawlers) {
            fetch(crawler);
        }
    }

    private void fetch(final FacilityCrawler crawler) {
        final FacilityInfoSource infoSource = crawler.getSupportingFacilityInfoSource();
        final Set<String> facilityCodes = facilityRepository.findAllFacilityCodeByInfoSource(infoSource);
        log.info("전체 {} 크롤링 시작", infoSource.getName());
        final long crawlStartTime = System.currentTimeMillis();
        final List<Facility> totalCrawledFacilities = crawler.crawl();
        final long crawlEndTime = System.currentTimeMillis();
        log.info("전체 {} 크롤링 완료.", infoSource.getName());
        log.info("전체 {} 크롤링된 개수: {}", infoSource.getName(), totalCrawledFacilities.size());
        final List<Facility> newFacilitiesToSave =
                totalCrawledFacilities.stream()
                                      .filter(facility -> !facilityCodes.contains(facility.getFacilityCode()))
                                      .toList();

        log.info("새로운 {} 개수: {}", infoSource.getName(), newFacilitiesToSave.size());

        log.info("{}개의 새로운 시설 좌표 찾기 시작", newFacilitiesToSave.size());
        final long locationInfoResolveStartTime = System.currentTimeMillis();
        for (final Facility facility : newFacilitiesToSave) {
            try {
                final LocationInfo locationInfo = locationInfoResolver.resolve(
                        facility.getJibunOrRoadAddress(),
                        facility.getName()
                );

                facility.updateLocationInfo(locationInfo);
            } catch (final LocationInfoResolveFailedException e) {
                log.error("시설({})의 위치 좌표를 찾을 수 없습니다: {}", facility.getName(), e.getMessage());
            }
        }
        final long locationInfoResolveEndTime = System.currentTimeMillis();
        log.info("{}개의 새로운 시설 좌표 찾기 완료", newFacilitiesToSave.size());

        log.info("{}개의 새로운 시설 저장 시작", newFacilitiesToSave.size());
        final long saveStartTime = System.currentTimeMillis();
        facilityRepository.saveAll(newFacilitiesToSave);
        final long saveEndTime = System.currentTimeMillis();
        log.info("{}개의 새로운 시설 저장 완료", newFacilitiesToSave.size());

        log.info("{}개의 전체 {} 크롤링 소요 시간: {} ms",
                totalCrawledFacilities.size(), infoSource.getName(), crawlEndTime - crawlStartTime
        );
        log.info("{}개의 새로운 {} 좌표 찾기 소요 시간: {} ms",
                newFacilitiesToSave.size(), infoSource.getName(), locationInfoResolveEndTime - locationInfoResolveStartTime
        );
        log.info("{}개의 새로운 {} 저장 소요 시간: {} ms",
                newFacilitiesToSave.size(), infoSource.getName(), saveEndTime - saveStartTime
        );
    }
}

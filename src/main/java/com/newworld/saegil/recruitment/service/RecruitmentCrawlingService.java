package com.newworld.saegil.recruitment.service;

import com.newworld.saegil.location.LocationInfo;
import com.newworld.saegil.location.LocationInfoResolveFailedException;
import com.newworld.saegil.location.LocationInfoResolver;
import com.newworld.saegil.recruitment.domain.Recruitment;
import com.newworld.saegil.recruitment.domain.RecruitmentInfoSource;
import com.newworld.saegil.recruitment.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecruitmentCrawlingService {

    private final Set<RecruitmentCrawler> crawlers;
    private final LocationInfoResolver locationInfoResolver;
    private final RecruitmentRepository recruitmentRepository;

    public void fetchRecruitments(final LocalDate date) {
        for (final RecruitmentCrawler crawler : crawlers) {
            fetch(crawler, date);
        }
    }

    private void fetch(final RecruitmentCrawler crawler, final LocalDate date) {
        final RecruitmentInfoSource infoSource = crawler.getSupportingRecruitmentInfoSource();
        log.info("전체 {} 크롤링 시작", infoSource.getName());
        final long crawlStartTime = System.currentTimeMillis();
        final List<Recruitment> totalCrawledRecruitments = crawler.crawl(date);
        final long crawlEndTime = System.currentTimeMillis();
        log.info("전체 {} 크롤링 완료.", infoSource.getName());
        log.info("전체 {} 크롤링된 개수: {}", infoSource.getName(), totalCrawledRecruitments.size());
        log.info("새로운 {} 개수: {}", infoSource.getName(), totalCrawledRecruitments.size());

        log.info("{}개의 새로운 채용정보 좌표 찾기 시작", totalCrawledRecruitments.size());
        final long locationInfoResolveStartTime = System.currentTimeMillis();
        for (final Recruitment recruitment : totalCrawledRecruitments) {
            try {
                final LocationInfo locationInfo = locationInfoResolver.resolve(
                        recruitment.getRoadAddress(),
                        ""
                );

                recruitment.updateLocationInfo(locationInfo);
            } catch (final LocationInfoResolveFailedException e) {
                recruitment.markError(e);
                log.error("채용정보({})의 근무지 위치 좌표를 찾을 수 없습니다: {}", recruitment.getName(), e.getMessage());
            } catch (final Exception e) {
                recruitment.markError(e);
                log.error("채용정보({})의 근무지 위치 좌표를 찾는 중 알 수 없는 오류가 발생했습니다: {}", recruitment.getName(), e.getMessage());
            }
        }
        final long locationInfoResolveEndTime = System.currentTimeMillis();
        log.info("{}개의 새로운 채용정보 근무지 좌표 찾기 완료", totalCrawledRecruitments.size());

        log.info("{}개의 새로운 채용정보 저장 시작", totalCrawledRecruitments.size());
        final long saveStartTime = System.currentTimeMillis();
        recruitmentRepository.saveAll(totalCrawledRecruitments);
        final long saveEndTime = System.currentTimeMillis();
        log.info("{}개의 새로운 채용정보 저장 완료", totalCrawledRecruitments.size());

        log.info("{}개의 전체 {} 크롤링 소요 시간: {} ms",
                totalCrawledRecruitments.size(), infoSource.getName(), crawlEndTime - crawlStartTime
        );
        log.info("{}개의 새로운 {} 좌표 찾기 소요 시간: {} ms",
                totalCrawledRecruitments.size(), infoSource.getName(), locationInfoResolveEndTime - locationInfoResolveStartTime
        );
        log.info("{}개의 새로운 {} 저장 소요 시간: {} ms",
                totalCrawledRecruitments.size(), infoSource.getName(), saveEndTime - saveStartTime
        );
    }
}

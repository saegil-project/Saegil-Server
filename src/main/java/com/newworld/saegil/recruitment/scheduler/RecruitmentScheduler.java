package com.newworld.saegil.recruitment.scheduler;

import com.newworld.saegil.recruitment.service.RecruitmentCrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.task.recruitment_scheduler.enabled", havingValue = "true")
public class RecruitmentScheduler {

    private final RecruitmentCrawlingService recruitmentCrawlingService;

    @EventListener(ApplicationReadyEvent.class)
    public void onStart() {
        fetchYesterdayRecruitments();
    }

    private void fetchYesterdayRecruitments() {
        final LocalDate yesterday = LocalDate.now().minusDays(1L);

        recruitmentCrawlingService.fetchRecruitments(yesterday);
    }

    // 매일 오전 9시에 실행
    @Scheduled(cron = "0 0 9 * * *")
    public void periodicRecruitmentCrawling() {
        fetchYesterdayRecruitments();
    }
}

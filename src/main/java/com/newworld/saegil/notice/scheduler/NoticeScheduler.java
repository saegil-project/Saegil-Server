package com.newworld.saegil.notice.scheduler;

import com.newworld.saegil.notice.service.NoticeCrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.task.notice_scheduler.enabled", havingValue = "true")
public class NoticeScheduler {

    private final NoticeCrawlingService noticeCrawlingService;

    @EventListener(ApplicationReadyEvent.class)
    public void onStart() {
        noticeCrawlingService.fetchNewNotices();
    }

    // 매 시간 30분에 실행
    @Scheduled(cron = "0 30 * * * *")
    public void periodicNoticeCheck() {
        noticeCrawlingService.fetchNewNotices();
    }
}

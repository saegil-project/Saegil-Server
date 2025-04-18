package com.newworld.saegil.notice.scheduler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.newworld.saegil.notice.service.NoticeService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.task.notice_scheduler.enabled", havingValue = "true")
public class NoticeScheduler {

    private final NoticeService noticeService;

    @EventListener(ApplicationReadyEvent.class)
    public void onStart() {
        noticeService.fetchNewNotices();
    }

    // 정각마다 실행
    @Scheduled(cron = "0 0 * * * *")
    public void periodicNoticeCheck() {
        noticeService.fetchNewNotices();
    }
}

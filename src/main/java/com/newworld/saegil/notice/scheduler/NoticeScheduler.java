package com.newworld.saegil.notice.scheduler;

import com.newworld.saegil.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
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

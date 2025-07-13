package com.newworld.saegil.notification.event;

import com.newworld.saegil.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationService notificationService;

    @Async
    @EventListener
    public void handleNotificationEvent(final NotificationEvent event) {
        try {
            if (event.getUserIds().size() == 1) {
                final Long userId = event.getUserIds().getFirst();
                notificationService.sendNotification(userId, event.getTitle(), event.getBody(), event.getData());
            } else {
                notificationService.sendNotificationToMultipleUsers(
                    event.getUserIds(), 
                    event.getTitle(), 
                    event.getBody(), 
                    event.getData()
                );
            }
            log.info("알림 이벤트 처리 완료 - 제목: {}, 사용자 수: {}", event.getTitle(), event.getUserIds().size());
        } catch (Exception e) {
            log.error("알림 이벤트 처리 중 오류 발생 - 제목: {}, 사용자 수: {}", 
                event.getTitle(), event.getUserIds().size(), e);
        }
    }
} 

package com.newworld.saegil.notification.service;

import com.newworld.saegil.notification.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationHelper {

    private final ApplicationEventPublisher eventPublisher;

    public void publishNotificationEvent(
        final String title,
        final String body,
        final Long userId
    ) {
        final NotificationEvent event = new NotificationEvent(this, title, body, userId);
        eventPublisher.publishEvent(event);
    }

    public void publishNotificationEvent(
        final String title,
        final String body,
        final Long userId,
        final Map<String, String> data
    ) {
        final NotificationEvent event = new NotificationEvent(this, title, body, userId, data);
        eventPublisher.publishEvent(event);
    }

    public void publishNotificationEvent(
        final String title,
        final String body,
        final List<Long> userIds
    ) {
        final NotificationEvent event = new NotificationEvent(this, title, body, userIds);
        eventPublisher.publishEvent(event);
    }

    public void publishNotificationEvent(
        final String title,
        final String body,
        final List<Long> userIds,
        final Map<String, String> data
    ) {
        final NotificationEvent event = new NotificationEvent(this, title, body, userIds, data);
        eventPublisher.publishEvent(event);
    }
} 

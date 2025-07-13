package com.newworld.saegil.notification.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;
import java.util.Map;

@Getter
public class NotificationEvent extends ApplicationEvent {

    private final String title;
    private final String body;
    private final List<Long> userIds;
    private final Map<String, String> data;

    public NotificationEvent(
        final Object source,
        final String title,
        final String body,
        final List<Long> userIds,
        final Map<String, String> data
    ) {
        super(source);
        this.title = title;
        this.body = body;
        this.userIds = userIds;
        this.data = data;
    }

    public NotificationEvent(
        final Object source,
        final String title,
        final String body,
        final Long userId,
        final Map<String, String> data
    ) {
        this(source, title, body, List.of(userId), data);
    }

    public NotificationEvent(
        final Object source,
        final String title,
        final String body,
        final List<Long> userIds
    ) {
        this(source, title, body, userIds, null);
    }

    public NotificationEvent(
        final Object source,
        final String title,
        final String body,
        final Long userId
    ) {
        this(source, title, body, List.of(userId), null);
    }
} 

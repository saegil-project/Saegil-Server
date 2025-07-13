package com.newworld.saegil.notification.service;

import java.util.List;
import java.util.Map;

public interface NotificationService {

    void sendNotification(final Long userId, final String title, final String body, final Map<String, String> data);

    void sendNotificationToMultipleUsers(final List<Long> userIds, final String title, final String body, final Map<String, String> data);
} 

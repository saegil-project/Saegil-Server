package com.newworld.saegil.notification.service;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.newworld.saegil.user.domain.User;
import com.newworld.saegil.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmNotificationService implements NotificationService {

    private static final int MAX_RETRY_COUNT = 4;
    private static final long RETRY_DELAY_MS = 1000;

    private final FirebaseMessaging firebaseMessaging;
    private final AndroidConfig highPriorityAndroidConfig;
    private final Executor fcmExecutor;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public void sendNotification(final Long userId, final String title, final String body, final Map<String, String> data) {
        final User user = userRepository.findById(userId)
                                        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        if (user.getDeviceToken() == null) {
            log.warn("User ID [{}]의 디바이스 토큰이 없습니다.", userId);
            return;
        }

        sendNotificationWithRetry(user.getDeviceToken(), title, body, data, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public void sendNotificationToMultipleUsers(final List<Long> userIds, final String title, final String body, final Map<String, String> data) {
        final List<User> users = userRepository.findAllById(userIds);
        final List<String> deviceTokens = users.stream()
                                               .map(User::getDeviceToken)
                                               .filter(Objects::nonNull)
                                               .toList();

        if (deviceTokens.isEmpty()) {
            log.warn("알림을 보낼 디바이스 토큰이 없습니다.");
            return;
        }
        final List<CompletableFuture<Void>> futures =
                deviceTokens.stream()
                            .map(token ->
                                    CompletableFuture.runAsync(
                                            () -> sendNotificationWithRetry(token, title, body, data, null),
                                            fcmExecutor
                                    )
                            ).toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private void sendNotificationWithRetry(final String deviceToken, final String title, final String body, final Map<String, String> data, final Long userId) {
        int retryCount = 0;
        Exception lastException = null;

        while (retryCount < MAX_RETRY_COUNT) {
            try {
                final Message message = createMessage(deviceToken, title, body, data);
                firebaseMessaging.send(message);
                log.info("FCM 알림 전송 성공 - 토큰: {}, 제목: {}", deviceToken, title);
                return;
            } catch (FirebaseMessagingException e) {
                lastException = e;
                retryCount++;

                if (retryCount < MAX_RETRY_COUNT) {
                    log.warn("FCM 알림 전송 실패 (재시도 {}/{}): 토큰: {}, 오류: {}",
                            retryCount, MAX_RETRY_COUNT, deviceToken, e.getMessage());
                    try {
                        Thread.sleep(RETRY_DELAY_MS * retryCount);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }

        log.error("FCM 알림 전송 최종 실패 ({}회 재시도 후): 토큰: {}, 제목: {}",
                MAX_RETRY_COUNT, deviceToken, title, lastException);

        if (userId != null) {
            log.error("사용자 {}의 알림 전송이 실패했습니다.", userId);
        }
    }

    private Message createMessage(final String deviceToken, final String title, final String body, final Map<String, String> data) {
        final Message.Builder messageBuilder = Message.builder()
                                                      .setToken(deviceToken)
                                                      .setAndroidConfig(highPriorityAndroidConfig)
                                                      .setNotification(
                                                              Notification.builder()
                                                                          .setTitle(title)
                                                                          .setBody(body)
                                                                          .build()
                                                      );

        if (data != null) {
            messageBuilder.putAllData(data);
        }

        return messageBuilder.build();
    }
} 

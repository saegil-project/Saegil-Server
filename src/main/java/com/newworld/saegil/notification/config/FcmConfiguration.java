package com.newworld.saegil.notification.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;

@Configuration
@RequiredArgsConstructor
public class FcmConfiguration {

    private final FcmProperties fcmProperties;

    @Bean
    public FirebaseMessaging firebaseMessaging(final FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            final ClassPathResource resource = new ClassPathResource(fcmProperties.getServiceAccountJsonPath());
            try (InputStream serviceAccount = resource.getInputStream()) {
                final FirebaseOptions options = FirebaseOptions.builder()
                                                               .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                                                               .build();
                FirebaseApp.initializeApp(options);
            }
        }
        return FirebaseApp.getInstance();
    }

    @Bean
    public AndroidConfig highPriorityAndroidConfig() {
        return AndroidConfig.builder()
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .build();
    }

    @Bean
    public Executor fcmExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("fcm-sender-");
        executor.initialize();
        return executor;
    }
} 

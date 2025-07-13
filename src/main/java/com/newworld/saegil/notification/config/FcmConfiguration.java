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

import java.io.IOException;
import java.io.InputStream;

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
            ClassPathResource resource = new ClassPathResource(fcmProperties.getServiceAccountJsonPath());
            InputStream serviceAccount = resource.getInputStream();

            FirebaseOptions options = FirebaseOptions.builder()
                                                     .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                                                     .build();

            FirebaseApp.initializeApp(options);
        }
        return FirebaseApp.getInstance();
    }

    @Bean
    public AndroidConfig highPriorityAndroidConfig() {
        return AndroidConfig.builder()
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .build();
    }
} 

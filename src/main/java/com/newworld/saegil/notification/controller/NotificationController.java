package com.newworld.saegil.notification.controller;

import com.newworld.saegil.authentication.annotation.AuthUser;
import com.newworld.saegil.authentication.dto.AuthUserInfo;
import com.newworld.saegil.configuration.SwaggerConfiguration;
import com.newworld.saegil.notification.service.NotificationHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "알림 테스트", description = "알림 전송 테스트 API")
public class NotificationController {

    private final NotificationHelper notificationHelper;

    @PostMapping("/test")
    @Operation(
            summary = "알림 전송 테스트 (테스트용)",
            description = """
                    로그인 시 device token을 함께 전달한 경우에만 정상적으로 동작합니다.
                    
                    알림 전송 테스트를 위해 사용됩니다.
                    
                    실제 서비스에서는 사용하지 않습니다.
                    """,
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    public String sendTestNotificationToSingleUser(
            @AuthUser AuthUserInfo authUserInfo,
            @RequestParam final String title,
            @RequestParam final String body
    ) {
        notificationHelper.publishNotificationEvent(title, body, authUserInfo.userId());

        return "알림 이벤트가 발행되었습니다.";
    }
}

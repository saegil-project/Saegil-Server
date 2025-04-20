package com.newworld.saegil.llm.controller;

import com.newworld.saegil.llm.config.ProxyProperties; // Import ProxyProperties
import com.newworld.saegil.llm.service.LlmProxyService; // Import to access nested records
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder; // Use RestTemplateBuilder for configuration
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

// Use WebEnvironment.NONE as we are not testing the Spring Boot server itself, just using its context
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
// Rename the class to reflect direct API testing
public class AssistantControllerDirectApiTest {

    @Autowired
    private ProxyProperties proxyProperties; // Inject properties to get FastAPI URL

    private final RestTemplate restTemplate; // Use a standard RestTemplate

    // Constructor injection for RestTemplate
    @Autowired
    public AssistantControllerDirectApiTest(RestTemplateBuilder builder) {
        // Configure RestTemplate if needed (e.g., timeouts, message converters)
        this.restTemplate = builder.build();
    }

    // Test questions provider remains the same
    private static Stream<String> provideQuestions() {
        return Stream.of(
                "여긴 장마당이 따로 없는 것 같은데, 살림거리를 어디 가서 사야 합네까?",
                "남조선에서는 장보는 데 ‘카드’라는 걸 쓴다던데, 그건 또 뭘 말하는 겁네까?",
                "여기선 물건 살 때 값흥정은 못합네까?",
                "몸이 아파 병원에 가고 싶은데, 여기선 진찰을 받으려면 어떻게 해야 하오? 진찰표는 어디서 떼야 하오?",
                "병원비가 너무 비싸다던데, 돈 없으면 치료 못 받습네까?",
                "약국에서 약을 사려면 무조건 처방전이 있어야 하오?",
                "신분증 같은 걸 만든다던데, 남조선에선 그걸 뭐라 부르오? 또 어떻게 만들어야 하오?",
                "집을 얻고 싶은데, 부동산은 뭘 하는 데입네까? 집문서는 누가 관리합네까?",
                "아이를 학교에 보내고 싶은데, 등록은 어디 가서 합네까?",
                "텔레비죤을 틀었는데 전부 모르는 말이오. 자막은 어떻게 켜는 거요?",
                "대학 다니고 싶은데, 입학시험은 또 어떤 식으로 보는 거요?",
                "일자리를 구하고 싶은데, 로동신문 같은 데서 보던 구인광고는 없습네까?",
                "남조선에선 아침 9시에 출근하고 저녁 6시에 퇴근한다던데, 그게 맞습네까?",
                "직장에선 말 끝마다 “~요”를 붙이던데, 말투를 꼭 그렇게 해야 하나요?",
                "남조선 말은 너무 빨라서 알아듣기 힘들어요. 전 표준말이랑 좀 다른데, 괜찮습네까?",
                "여긴 왜 다들 “네~”만 합니까? 무슨 뜻입네까?"
        );
    }

    @DisplayName("다양한 질문으로 FastAPI Assistant 직접 호출 테스트")
    @ParameterizedTest
    @MethodSource("provideQuestions")
    void testFastApiAssistantDirectly(String question) {
        // Given: Prepare the request for the FastAPI endpoint using ProxyProperties
        String assistantPath = proxyProperties.getAssistantPath(); // Get the full path from properties
        if (!assistantPath.endsWith("/")) {
            assistantPath += "/";
        }

        // Build URL without thread_id for the first request
        String requestUrl = UriComponentsBuilder.fromUriString(assistantPath)
                .build().toUriString();

        // Use the ProxyAssistantRequest record defined within LlmProxyService
        LlmProxyService.ProxyAssistantRequest requestBody = new LlmProxyService.ProxyAssistantRequest(question);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LlmProxyService.ProxyAssistantRequest> requestEntity = new HttpEntity<>(requestBody, headers);

        System.out.println("Requesting FastAPI URL: " + requestUrl);
        System.out.println("Request Body: " + requestBody);


        // When: Make the actual HTTP POST request directly to FastAPI
        ResponseEntity<LlmProxyService.ProxyAssistantResponse> responseEntity = restTemplate.exchange(
                requestUrl,
                HttpMethod.POST,
                requestEntity,
                LlmProxyService.ProxyAssistantResponse.class // Use the ProxyAssistantResponse record
        );

        // Then: Verify the response status and print the response text
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        LlmProxyService.ProxyAssistantResponse responseBody = responseEntity.getBody();
        assertThat(responseBody).isNotNull();
        // FastAPI might return the original text in a different field or not at all, adjust if needed
        // assertThat(responseBody.text()).isEqualTo(question);
        assertThat(responseBody.threadId()).isNotNull().isNotEmpty();
        assertThat(responseBody.response()).isNotNull().isNotEmpty();

        // Print the actual response text to the console
        System.out.println("질문: " + question);
        System.out.println("응답: " + responseBody.response());
        System.out.println("Thread ID: " + responseBody.threadId());
        System.out.println("--------------------------------------------------");

        // --- Optional: Test with existing thread ID ---
        String returnedThreadId = responseBody.threadId();
        String followUpQuestion = "방금 질문에 대해 좀 더 자세히 설명해주세요.";

        // Build URL with the returned thread_id
        String followUpUrl = UriComponentsBuilder.fromUriString(assistantPath)
                .queryParam("thread_id", returnedThreadId)
                .build().toUriString();

        LlmProxyService.ProxyAssistantRequest followUpRequestBody = new LlmProxyService.ProxyAssistantRequest(followUpQuestion);
        HttpEntity<LlmProxyService.ProxyAssistantRequest> followUpRequestEntity = new HttpEntity<>(followUpRequestBody, headers);

        System.out.println("Requesting FastAPI URL (Follow-up): " + followUpUrl);
        System.out.println("Request Body (Follow-up): " + followUpRequestBody);

        ResponseEntity<LlmProxyService.ProxyAssistantResponse> followUpResponseEntity = restTemplate.exchange(
                followUpUrl,
                HttpMethod.POST,
                followUpRequestEntity,
                LlmProxyService.ProxyAssistantResponse.class
        );

        assertThat(followUpResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        LlmProxyService.ProxyAssistantResponse followUpResponseBody = followUpResponseEntity.getBody();
        assertThat(followUpResponseBody).isNotNull();
        assertThat(followUpResponseBody.threadId()).isEqualTo(returnedThreadId); // Verify thread ID is maintained
        assertThat(followUpResponseBody.response()).isNotNull().isNotEmpty();

        System.out.println("후속 질문: " + followUpQuestion);
        System.out.println("후속 응답: " + followUpResponseBody.response());
        System.out.println("Thread ID: " + followUpResponseBody.threadId());
        System.out.println("==================================================");

    }
}

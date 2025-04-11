package com.newworld.saegil.proxy.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * 프록시 설정을 위한 구성 속성.
 * 이 클래스는 application.yml의 프록시 구성을 Java 속성으로 매핑합니다.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "proxy")
public class ProxyProperties {

    /**
     * 프록시 요청을 위한 기본 대상 URL
     */
    private String targetUrl;

    /**
     * 서버 이름과 해당 URL의 맵
     */
    private Map<String, String> servers;

    /**
     * 연결 타임아웃(밀리초)
     */
    private int connectTimeoutMs = 10000;

    /**
     * 읽기 타임아웃(밀리초)
     */
    private int readTimeoutMs = 10000;

    /**
     * 쓰기 타임아웃(밀리초)
     */
    private int writeTimeoutMs = 10000;

    /**
     * 응답 타임아웃(밀리초)
     */
    private int responseTimeoutMs = 10000;

    /**
     * 최대 연결 수
     */
    private int maxConnections = 500;

    /**
     * 최대 유휴 시간(초)
     */
    private int maxIdleTimeSeconds = 20;

    /**
     * 최대 수명 시간(분)
     */
    private int maxLifeTimeMinutes = 5;

    /**
     * 대기 중인 획득 타임아웃(초)
     */
    private int pendingAcquireTimeoutSeconds = 60;

    /**
     * 백그라운드 제거 시간(초)
     */
    private int evictBackgroundSeconds = 120;

    /**
     * 최대 메모리 내 크기(메가바이트)
     */
    private int maxInMemorySizeMb = 16;

    /**
     * 재시도 활성화 여부
     */
    private boolean retryEnabled = true;

    /**
     * 최대 재시도 횟수
     */
    private int maxRetries = 3;

    /**
     * 재시도 지연 시간(밀리초)
     */
    private int retryDelayMs = 1000;

    /**
     * FastAPI LLM 서버의 URL을 가져옵니다
     * @return FastAPI LLM 서버의 URL
     */
    public String getLlmServerUrl() {
        return servers.getOrDefault("llm-service", "http://localhost:9090");
    }
}

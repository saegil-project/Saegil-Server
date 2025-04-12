package com.newworld.saegil.proxy.config;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

/**
 * 프록시 서비스에서 사용되는 WebClient 구성.
 */
@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private static final Logger logger = LoggerFactory.getLogger(WebClientConfig.class);

    private final ProxyProperties proxyProperties;

    /**
     * 공통 구성으로 WebClient 빌더를 생성합니다.
     *
     * @return 공통 구성이 적용된 WebClient.Builder
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        // 연결 제공자 구성
        ConnectionProvider provider = ConnectionProvider.builder("proxy-connection-provider")
                                                        .maxConnections(proxyProperties.getMaxConnections())
                                                        .maxIdleTime(Duration.ofSeconds(
                                                                proxyProperties.getMaxIdleTimeSeconds()))
                                                        .maxLifeTime(Duration.ofMinutes(
                                                                proxyProperties.getMaxLifeTimeMinutes()))
                                                        .pendingAcquireTimeout(Duration.ofSeconds(
                                                                proxyProperties.getPendingAcquireTimeoutSeconds()))
                                                        .evictInBackground(Duration.ofSeconds(
                                                                proxyProperties.getEvictBackgroundSeconds()))
                                                        .build();

        // HTTP 클라이언트 구성
        HttpClient httpClient = HttpClient.create(provider)
                                          .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
                                                  proxyProperties.getConnectTimeoutMs())
                                          .responseTimeout(Duration.ofMillis(proxyProperties.getResponseTimeoutMs()))
                                          .doOnConnected(conn -> conn
                                                  .addHandlerLast(
                                                          new ReadTimeoutHandler(proxyProperties.getReadTimeoutMs(),
                                                                  TimeUnit.MILLISECONDS))
                                                  .addHandlerLast(
                                                          new WriteTimeoutHandler(proxyProperties.getWriteTimeoutMs(),
                                                                  TimeUnit.MILLISECONDS)));

        // 메모리 제한을 위한 교환 전략 구성
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                                                                  .codecs(configurer -> configurer.defaultCodecs()
                                                                                                  .maxInMemorySize(
                                                                                                          proxyProperties.getMaxInMemorySizeMb()
                                                                                                                  * 1024
                                                                                                                  * 1024))
                                                                  .build();

        // WebClient 빌더 생성
        return WebClient.builder()
                        .clientConnector(new ReactorClientHttpConnector(httpClient))
                        .exchangeStrategies(exchangeStrategies)
                        .filter(logRequest())
                        .filter(logResponse());
    }

    /**
     * FastAPI LLM 서버를 위한 WebClient를 생성합니다.
     *
     * @param webClientBuilder WebClient 빌더
     * @return FastAPI LLM 서버를 위한 WebClient
     */
    @Bean
    public WebClient llmServerWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(proxyProperties.getLlmServerUrl())
                .build();
    }

    /**
     * 요청 로깅 필터 함수.
     *
     * @return 요청을 로깅하기 위한 ExchangeFilterFunction
     */
    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            logger.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            return Mono.just(clientRequest);
        });
    }

    /**
     * 응답 로깅 필터 함수.
     *
     * @return 응답을 로깅하기 위한 ExchangeFilterFunction
     */
    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            logger.info("Response status: {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }
}

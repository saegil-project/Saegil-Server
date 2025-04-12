# Saegil 프록시 패키지 설명서

## 개요

Saegil 프록시 패키지(`com.newworld.saegil.proxy`)는 Spring Boot 애플리케이션에서 FastAPI LLM 서버와의 통신을 담당하는 컴포넌트입니다. 이 패키지는 텍스트-음성 변환(
TTS), 음성-텍스트 변환(STT), ChatGPT 응답 생성 등의 기능을 제공하는 FastAPI 서버로 요청을 프록시하고 응답을 클라이언트에게 전달합니다.

## 패키지 구조

프록시 패키지는 다음과 같은 구조로 구성되어 있습니다:

```
com.newworld.saegil.proxy/
├── config/                 # 프록시 구성 관련 클래스
│   ├── ProxyProperties.java    # 프록시 설정 속성
│   └── WebClientConfig.java    # WebClient 구성
├── controller/             # API 엔드포인트 컨트롤러
│   └── LlmProxyController.java # LLM 프록시 컨트롤러
├── dto/                    # 데이터 전송 객체
│   ├── request/            # 요청 DTO
│   │   ├── ChatGptAudioUrlRequest.java
│   │   ├── ChatGptSttRequest.java
│   │   ├── ChatGptTextRequest.java
│   │   ├── SpeechToTextUrlRequest.java
│   │   └── TextToSpeechRequest.java
│   └── response/           # 응답 DTO
│       ├── ChatGptResponse.java
│       └── SpeechToTextResponse.java
└── service/                # 서비스 계층
    ├── LlmProxyService.java    # 프록시 서비스 인터페이스
    └── LlmProxyServiceImpl.java # 프록시 서비스 구현체
```

## 주요 컴포넌트 설명

### 1. 설정 (Configuration)

#### ProxyProperties.java

이 클래스는 프록시 설정을 위한 구성 속성을 정의합니다. `application.yml` 파일의 `proxy` 접두사를 가진 속성들을 Java 객체로 매핑합니다.

주요 속성:

- `targetUrl`: 프록시 요청을 위한 기본 대상 URL
- `servers`: 서버 이름과 해당 URL의 맵
- 연결 타임아웃 설정: `connectTimeoutMs`, `readTimeoutMs`, `writeTimeoutMs`, `responseTimeoutMs`
- 연결 풀 설정: `maxConnections`, `maxIdleTimeSeconds`, `maxLifeTimeMinutes` 등
- 메모리 제한: `maxInMemorySizeMb`
- 재시도 설정: `retryEnabled`, `maxRetries`, `retryDelayMs`

`getLlmServerUrl()` 메서드는 FastAPI LLM 서버의 URL을 반환합니다. 기본값은 "http://localhost:9090"입니다.

#### WebClientConfig.java

이 클래스는 프록시 서비스에서 사용되는 WebClient를 구성합니다. WebClient는 Spring의 비동기 HTTP 클라이언트로, 리액티브 프로그래밍 방식으로 HTTP 요청을 처리합니다.

주요 기능:

- 연결 풀 구성: 최대 연결 수, 유휴 시간, 수명 시간 등 설정
- HTTP 클라이언트 구성: 연결 타임아웃, 응답 타임아웃, 읽기/쓰기 타임아웃 설정
- 메모리 제한 설정: 요청/응답 데이터의 메모리 사용량 제한
- 로깅 필터: 요청 및 응답 로깅을 위한 필터 추가
- LLM 서버용 WebClient 빈 생성: FastAPI LLM 서버와 통신하기 위한 WebClient 생성

### 2. 컨트롤러 (Controller)

#### LlmProxyController.java

이 클래스는 클라이언트의 요청을 받아 LLM 서비스로 프록시하는 REST API 엔드포인트를 제공합니다.

주요 엔드포인트:

- `POST /api/v1/llm/text-to-speech`: 텍스트를 음성으로 변환 (ElevenLabs API 사용)
- `POST /api/v1/llm/speech-to-text/url`: URL에서 음성을 텍스트로 변환 (OpenAI Whisper API 사용)
- `POST /api/v1/llm/speech-to-text/upload`: 업로드된 파일에서 음성을 텍스트로 변환
- `POST /api/v1/llm/chatgpt/text`: 텍스트로부터 ChatGPT 응답 받기
- `POST /api/v1/llm/chatgpt/stt`: STT로 변환된 텍스트로부터 ChatGPT 응답 받기
- `POST /api/v1/llm/chatgpt/audio-url`: 오디오 URL로부터 ChatGPT 응답 받기
- `POST /api/v1/llm/chatgpt/upload`: 업로드된 오디오 파일로부터 ChatGPT 응답 받기

모든 엔드포인트는 리액티브 프로그래밍 방식으로 구현되어 있으며, `Mono` 객체를 반환합니다. 이는 비동기적으로 요청을 처리하고 응답을 반환하는 것을 의미합니다.

### 3. 서비스 (Service)

#### LlmProxyService.java

이 인터페이스는 LLM 프록시 서비스의 메서드를 정의합니다.

주요 메서드:

- `textToSpeech(TextToSpeechRequest)`: 텍스트를 음성으로 변환
- `speechToTextFromUrl(SpeechToTextUrlRequest)`: URL에서 음성을 텍스트로 변환
- `speechToTextFromFile(FilePart)`: 업로드된 파일에서 음성을 텍스트로 변환
- `chatGptFromText(ChatGptTextRequest)`: 텍스트로부터 ChatGPT 응답 받기
- `chatGptFromStt(ChatGptSttRequest)`: STT로 변환된 텍스트로부터 ChatGPT 응답 받기
- `chatGptFromAudioUrl(ChatGptAudioUrlRequest)`: 오디오 URL로부터 ChatGPT 응답 받기
- `chatGptFromFile(FilePart)`: 업로드된 오디오 파일로부터 ChatGPT 응답 받기

#### LlmProxyServiceImpl.java

이 클래스는 `LlmProxyService` 인터페이스의 구현체로, WebClient를 사용하여 FastAPI LLM 서버와 통신합니다.

주요 특징:

- WebClient를 사용한 비동기 HTTP 요청 처리
- 요청 및 응답 로깅
- 재시도 로직 (3회 재시도, 1초 간격)
- JSON 및 멀티파트 폼 데이터 요청 처리

각 메서드는 다음과 같은 패턴으로 구현되어 있습니다:

1. 요청 로깅
2. WebClient를 사용하여 FastAPI LLM 서버에 POST 요청 전송
3. 콘텐츠 타입 설정 (JSON 또는 멀티파트 폼 데이터)
4. 요청 본문 설정
5. 응답 수신 및 적절한 타입으로 변환
6. 재시도 로직 추가
7. 오류 로깅

### 4. DTO (Data Transfer Objects)

#### 요청 DTO

- `TextToSpeechRequest`: 텍스트를 음성으로 변환하기 위한 요청 DTO (필드: text)
- `SpeechToTextUrlRequest`: URL에서 음성을 텍스트로 변환하기 위한 요청 DTO (필드: audioUrl)
- `ChatGptTextRequest`: 텍스트로부터 ChatGPT 응답을 받기 위한 요청 DTO (필드: text)
- `ChatGptSttRequest`: STT로 변환된 텍스트로부터 ChatGPT 응답을 받기 위한 요청 DTO (필드: audioText)
- `ChatGptAudioUrlRequest`: 오디오 URL로부터 ChatGPT 응답을 받기 위한 요청 DTO (필드: audioUrl)

#### 응답 DTO

- `SpeechToTextResponse`: 음성을 텍스트로 변환한 결과를 담는 응답 DTO (필드: text)
- `ChatGptResponse`: ChatGPT 응답을 담는 응답 DTO (필드: response)

## 동작 방식

1. 클라이언트가 Saegil 서버의 API 엔드포인트(`/api/v1/llm/*`)로 요청을 보냅니다.
2. `LlmProxyController`가 요청을 받아 적절한 `LlmProxyService` 메서드를 호출합니다.
3. `LlmProxyServiceImpl`은 WebClient를 사용하여 FastAPI LLM 서버로 요청을 전달합니다.
4. FastAPI LLM 서버는 요청을 처리하고 결과를 반환합니다:
    - 텍스트-음성 변환: ElevenLabs API 사용
    - 음성-텍스트 변환: OpenAI Whisper API 사용
    - ChatGPT 응답: OpenAI GPT-4o 모델 사용
5. `LlmProxyServiceImpl`은 FastAPI LLM 서버의 응답을 받아 적절한 DTO로 변환하여 반환합니다.
6. `LlmProxyController`는 서비스로부터 받은 응답을 클라이언트에게 전달합니다.

## 설정 방법

프록시 패키지는 `application.yml` 파일을 통해 구성할 수 있습니다. 다음은 설정 예시입니다:

```yaml
proxy:
  servers:
    llm-service: http://localhost:9090  # FastAPI LLM 서버 URL
  connectTimeoutMs: 10000               # 연결 타임아웃(밀리초)
  readTimeoutMs: 10000                  # 읽기 타임아웃(밀리초)
  writeTimeoutMs: 10000                 # 쓰기 타임아웃(밀리초)
  responseTimeoutMs: 10000              # 응답 타임아웃(밀리초)
  maxConnections: 500                   # 최대 연결 수
  maxIdleTimeSeconds: 20                # 최대 유휴 시간(초)
  maxLifeTimeMinutes: 5                 # 최대 수명 시간(분)
  pendingAcquireTimeoutSeconds: 60      # 대기 중인 획득 타임아웃(초)
  evictBackgroundSeconds: 120           # 백그라운드 제거 시간(초)
  maxInMemorySizeMb: 16                 # 최대 메모리 내 크기(메가바이트)
  retryEnabled: true                    # 재시도 활성화 여부
  maxRetries: 3                         # 최대 재시도 횟수
  retryDelayMs: 1000                    # 재시도 지연 시간(밀리초)
```

## 결론

Saegil 프록시 패키지는 Spring Boot 애플리케이션과 FastAPI LLM 서버 사이의 통신을 담당하는 중요한 컴포넌트입니다. 이 패키지는 리액티브 프로그래밍 방식으로 구현되어 있어 비동기적으로 요청을
처리하고, 연결 풀링, 타임아웃 처리, 재시도 로직 등을 통해 안정적인 통신을 보장합니다.

프록시 패키지를 통해 클라이언트는 텍스트-음성 변환, 음성-텍스트 변환, ChatGPT 응답 생성 등의 기능을 쉽게 사용할 수 있으며, 이러한 기능은 FastAPI LLM 서버에서 ElevenLabs API,
OpenAI Whisper API, OpenAI GPT-4o 모델을 사용하여 구현됩니다.

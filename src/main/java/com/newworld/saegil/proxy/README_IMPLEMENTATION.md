# LLM 프록시 구현

이 패키지는 모바일 앱에서 FastAPI LLM 서버로 요청을 전달하는 프록시 서비스의 구현을 포함하고 있습니다. FastAPI 서버는 텍스트-음성 변환, 음성-텍스트 변환, 그리고 ChatGPT 기능을 제공합니다.

## 아키텍처

프록시 서비스는 계층화된 아키텍처를 따릅니다:

1. **컨트롤러 계층**: 모바일 앱에서 HTTP 요청을 처리하고 서비스 계층으로 전달합니다.
2. **서비스 계층**: FastAPI 서버로 요청을 프록시하는 비즈니스 로직을 포함합니다.
3. **설정 계층**: WebClient 및 기타 구성 요소를 설정합니다.
4. **DTO 계층**: 요청/응답을 위한 데이터 전송 객체를 포함합니다.

## 구성 요소

### 설정

- **ProxyProperties**: application.yml의 프록시 설정을 Java 속성에 매핑합니다.
- **WebClientConfig**: FastAPI 서버에 요청을 보내기 위한 WebClient를 설정합니다.

### DTOs

- **TextToSpeechRequest**: 텍스트-음성 변환을 위한 요청 DTO.
- **SpeechToTextUrlRequest**: 오디오 URL을 사용한 음성-텍스트 변환을 위한 요청 DTO.
- **SpeechToTextResponse**: 음성-텍스트 변환을 위한 응답 DTO.
- **ChatGptTextRequest**: 텍스트를 사용한 ChatGPT 응답을 위한 요청 DTO.
- **ChatGptSttRequest**: STT 변환 텍스트를 사용한 ChatGPT 응답을 위한 요청 DTO.
- **ChatGptAudioUrlRequest**: 오디오 URL을 사용한 ChatGPT 응답을 위한 요청 DTO.
- **ChatGptResponse**: ChatGPT API를 위한 응답 DTO.

### 서비스

- **LlmProxyService**: 프록시 서비스를 위한 인터페이스.
- **LlmProxyServiceImpl**: 프록시 서비스의 구현.

### 컨트롤러

- **LlmProxyController**: 모바일 앱에서의 요청을 처리하기 위한 컨트롤러.

## API 엔드포인트

프록시 서비스는 다음과 같은 엔드포인트를 제공합니다:

- **POST /api/llm/text-to-speech**: 텍스트를 음성으로 변환합니다.
- **POST /api/llm/speech-to-text/url**: 오디오 URL에서 음성을 텍스트로 변환합니다.
- **POST /api/llm/speech-to-text/upload**: 업로드된 파일에서 음성을 텍스트로 변환합니다.
- **POST /api/llm/chatgpt/text**: 텍스트에서 ChatGPT 응답을 가져옵니다.
- **POST /api/llm/chatgpt/stt**: STT 변환 텍스트에서 ChatGPT 응답을 가져옵니다.
- **POST /api/llm/chatgpt/audio-url**: 오디오 URL에서 ChatGPT 응답을 가져옵니다.
- **POST /api/llm/chatgpt/upload**: 업로드된 오디오 파일에서 ChatGPT 응답을 가져옵니다.

## 설정

프록시 서비스는 application.yml 파일에서 설정됩니다:

```yaml
proxy:
  servers:
    llm-service: ${PROXY_LLM_SERVICE_URL:http://localhost:9090}
  connect-timeout-ms: 10000
  read-timeout-ms: 10000
  write-timeout-ms: 10000
  response-timeout-ms: 10000
  max-connections: 500
  max-idle-time-seconds: 20
  max-life-time-minutes: 5
  pending-acquire-timeout-seconds: 60
  evict-background-seconds: 120
  max-in-memory-size-mb: 16
  retry-enabled: true
  max-retries: 3
  retry-delay-ms: 1000
```

## 사용법

### 모바일 앱 통합

모바일 앱은 API 엔드포인트를 사용하여 프록시 서비스에 요청을 보낼 수 있습니다. 예를 들면:

```kotlin
// 텍스트-음성 변환
val textToSpeechRequest = TextToSpeechRequest(text = "Hello, world!")
val response = api.textToSpeech(textToSpeechRequest)

// URL에서 음성-텍스트 변환
val speechToTextUrlRequest = SpeechToTextUrlRequest(audioUrl = "https://example.com/audio.mp3")
val response = api.speechToTextFromUrl(speechToTextUrlRequest)

// 텍스트에서 ChatGPT 응답
val chatGptTextRequest = ChatGptTextRequest(text = "What is the weather like today?")
val response = api.chatGptFromText(chatGptTextRequest)
```

### 파일 업로드

파일 업로드 엔드포인트의 경우, 모바일 앱은 멀티파트 폼 데이터를 사용해야 합니다:

```kotlin
// 파일에서 음성-텍스트 변환
val audioFile = File("audio.mp3")
val audioRequestBody = MultipartBody.Builder()
    .setType(MultipartBody.FORM)
    .addFormDataPart(
        "file", audioFile.name,
        RequestBody.create(MediaType.parse("audio/mpeg"), audioFile)
    )
    .build()
val sttResponse = api.speechToTextFromFile(audioRequestBody)

// 파일에서 ChatGPT 응답
val chatAudioFile = File("audio.mp3")
val chatRequestBody = MultipartBody.Builder()
    .setType(MultipartBody.FORM)
    .addFormDataPart(
        "file", chatAudioFile.name,
        RequestBody.create(MediaType.parse("audio/mpeg"), chatAudioFile)
    )
    .build()
val chatResponse = api.chatGptFromFile(chatRequestBody)
```

## 오류 처리

프록시 서비스는 일시적인 오류를 처리하기 위한 재시도 로직을 포함합니다. FastAPI 서버를 사용할 수 없거나 오류를 반환하는 경우, 프록시 서비스는 최대 3번까지 1초 간격으로 요청을 재시도합니다.

모든 오류는 디버깅을 위해 로깅됩니다.

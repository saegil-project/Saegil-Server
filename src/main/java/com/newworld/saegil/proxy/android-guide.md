# LlmProxyController 안드로이드 개발자 가이드

## 개요

이 가이드는 Saegil 서버의 LlmProxyController를 안드로이드 애플리케이션에서 사용하는 방법을 설명합니다. Jetpack Compose와 Kotlin을 사용하여 텍스트-음성 변환(TTS),
음성-텍스트 변환(STT), ChatGPT 응답 생성 등의 기능을 구현하는 방법을 다룹니다.

## 목차

1. [설정](#설정)
2. [API 엔드포인트 개요](#api-엔드포인트-개요)
3. [텍스트를 음성으로 변환 (TTS)](#텍스트를-음성으로-변환-tts)
4. [URL에서 음성을 텍스트로 변환 (STT)](#url에서-음성을-텍스트로-변환-stt)
5. [파일에서 음성을 텍스트로 변환 (STT)](#파일에서-음성을-텍스트로-변환-stt)
6. [텍스트로부터 ChatGPT 응답 받기](#텍스트로부터-chatgpt-응답-받기)
7. [STT 텍스트로부터 ChatGPT 응답 받기](#stt-텍스트로부터-chatgpt-응답-받기)
8. [오디오 URL로부터 ChatGPT 응답 받기](#오디오-url로부터-chatgpt-응답-받기)
9. [오디오 파일로부터 ChatGPT 응답 받기](#오디오-파일로부터-chatgpt-응답-받기)
10. [에러 처리](#에러-처리)
11. [전체 구현 흐름](#전체-구현-흐름)

## 설정

안드로이드 프로젝트에서 Saegil 서버와 통신하기 위해 필요한 라이브러리와 설정을 추가합니다.

### 필요한 라이브러리

`build.gradle.kts` (앱 모듈)에 다음 의존성을 추가합니다:

```kotlin
dependencies {
    // Retrofit for network requests
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp for HTTP client
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Coroutines for asynchronous programming
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // Jetpack Compose
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.compose.runtime:runtime-livedata:1.5.4")
}
```

## API 엔드포인트 개요

Saegil 서버의 LlmProxyController는 다음과 같은 엔드포인트를 제공합니다:

1. **텍스트를 음성으로 변환 (TTS)**
    - 엔드포인트: `/api/v1/llm/text-to-speech`
    - 메소드: POST
    - 요청 형식: JSON (`{"text": "변환할 텍스트"}`)
    - 응답: MP3 형식의 오디오 스트림

2. **URL에서 음성을 텍스트로 변환 (STT)**
    - 엔드포인트: `/api/v1/llm/speech-to-text/url`
    - 메소드: POST
    - 요청 형식: JSON (`{"audioUrl": "오디오 파일의 URL"}`)
    - 응답: JSON (`{"text": "변환된 텍스트"}`)

3. **파일에서 음성을 텍스트로 변환 (STT)**
    - 엔드포인트: `/api/v1/llm/speech-to-text/upload`
    - 메소드: POST
    - 요청 형식: 멀티파트 폼 데이터 (파일 필드 이름: `file`)
    - 응답: JSON (`{"text": "변환된 텍스트"}`)

4. **텍스트로부터 ChatGPT 응답 받기**
    - 엔드포인트: `/api/v1/llm/chatgpt/text`
    - 메소드: POST
    - 요청 형식: JSON (`{"text": "ChatGPT에게 보낼 텍스트"}`)
    - 응답: JSON (`{"response": "ChatGPT의 응답"}`)

5. **STT 텍스트로부터 ChatGPT 응답 받기**
    - 엔드포인트: `/api/v1/llm/chatgpt/stt`
    - 메소드: POST
    - 요청 형식: JSON (`{"audioText": "STT로 변환된 텍스트"}`)
    - 응답: JSON (`{"response": "ChatGPT의 응답"}`)

6. **오디오 URL로부터 ChatGPT 응답 받기**
    - 엔드포인트: `/api/v1/llm/chatgpt/audio-url`
    - 메소드: POST
    - 요청 형식: JSON (`{"audioUrl": "오디오 파일의 URL"}`)
    - 응답: JSON (`{"response": "ChatGPT의 응답"}`)

7. **오디오 파일로부터 ChatGPT 응답 받기**
    - 엔드포인트: `/api/v1/llm/chatgpt/upload`
    - 메소드: POST
    - 요청 형식: 멀티파트 폼 데이터 (파일 필드 이름: `file`)
    - 응답: JSON (`{"response": "ChatGPT의 응답"}`)

## 텍스트를 음성으로 변환 (TTS)

텍스트를 음성으로 변환하는 기능을 구현하는 방법을 설명합니다.

### 구현 단계

1. **데이터 클래스 정의**

```kotlin
data class TextToSpeechRequest(val text: String)
```

2. **API 인터페이스 정의**

```kotlin
interface SaegilApiService {
    @POST("/api/v1/llm/text-to-speech")
    suspend fun textToSpeech(@Body request: TextToSpeechRequest): Response<ResponseBody>
}
```

3. **ViewModel 구현**

ViewModel에서는 다음 기능을 구현해야 합니다:

- 텍스트를 음성으로 변환하는 API 호출
- 응답으로 받은 오디오 데이터 처리
- MediaPlayer를 사용하여 오디오 재생

4. **Compose UI 구현**

UI에서는 다음 요소를 포함해야 합니다:

- 텍스트 입력 필드
- 변환 버튼
- 로딩 인디케이터
- 오디오 재생 버튼
- 에러 메시지 표시

### 사용 예시

```kotlin
// ViewModel에서 텍스트를 음성으로 변환
viewModel.convertTextToSpeech("안녕하세요, 반갑습니다.")

// 오디오 재생
viewModel.playAudio(context, audioData)
```

## URL에서 음성을 텍스트로 변환 (STT)

URL에서 음성을 텍스트로 변환하는 기능을 구현하는 방법을 설명합니다.

### 구현 단계

1. **데이터 클래스 정의**

```kotlin
data class SpeechToTextUrlRequest(val audioUrl: String)
data class SpeechToTextResponse(val text: String)
```

2. **API 인터페이스 정의**

```kotlin
interface SaegilApiService {
    @POST("/api/v1/llm/speech-to-text/url")
    suspend fun speechToTextFromUrl(@Body request: SpeechToTextUrlRequest): Response<SpeechToTextResponse>
}
```

3. **ViewModel 구현**

ViewModel에서는 다음 기능을 구현해야 합니다:

- URL에서 음성을 텍스트로 변환하는 API 호출
- 응답으로 받은 텍스트 처리

4. **Compose UI 구현**

UI에서는 다음 요소를 포함해야 합니다:

- URL 입력 필드
- 변환 버튼
- 로딩 인디케이터
- 변환된 텍스트 표시
- 에러 메시지 표시

### 사용 예시

```kotlin
// ViewModel에서 URL에서 음성을 텍스트로 변환
viewModel.convertSpeechToText("https://example.com/audio/sample.mp3")
```

## 파일에서 음성을 텍스트로 변환 (STT)

안드로이드 기기에서 오디오 파일을 선택하여 텍스트로 변환하는 기능을 구현하는 방법을 설명합니다.

### 구현 단계

1. **API 인터페이스 정의**

```kotlin
interface SaegilApiService {
    @Multipart
    @POST("/api/v1/llm/speech-to-text/upload")
    suspend fun speechToTextFromFile(@Part file: MultipartBody.Part): Response<SpeechToTextResponse>
}
```

2. **ViewModel 구현**

ViewModel에서는 다음 기능을 구현해야 합니다:

- 선택한 오디오 파일을 임시 파일로 변환
- 멀티파트 요청 생성
- 파일에서 음성을 텍스트로 변환하는 API 호출
- 응답으로 받은 텍스트 처리

3. **Compose UI 구현**

UI에서는 다음 요소를 포함해야 합니다:

- 파일 선택 버튼
- 선택된 파일 정보 표시
- 변환 버튼
- 로딩 인디케이터
- 변환된 텍스트 표시
- 에러 메시지 표시

### 사용 예시

```kotlin
// 파일 선택기 설정
val audioPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
) { uri: Uri? ->
    if (uri != null) {
        viewModel.convertSpeechToText(context, uri)
    }
}

// 파일 선택기 실행
audioPickerLauncher.launch("audio/*")
```

## 텍스트로부터 ChatGPT 응답 받기

텍스트 입력을 기반으로 ChatGPT의 응답을 받는 기능을 구현하는 방법을 설명합니다.

### 구현 단계

1. **데이터 클래스 정의**

```kotlin
data class ChatGptTextRequest(val text: String)
data class ChatGptResponse(val response: String)
```

2. **API 인터페이스 정의**

```kotlin
interface SaegilApiService {
    @POST("/api/v1/llm/chatgpt/text")
    suspend fun chatGptFromText(@Body request: ChatGptTextRequest): Response<ChatGptResponse>
}
```

3. **ViewModel 구현**

ViewModel에서는 다음 기능을 구현해야 합니다:

- 텍스트를 기반으로 ChatGPT 응답을 받는 API 호출
- 응답으로 받은 텍스트 처리

4. **Compose UI 구현**

UI에서는 다음 요소를 포함해야 합니다:

- 텍스트 입력 필드
- 응답 받기 버튼
- 로딩 인디케이터
- ChatGPT 응답 표시
- 에러 메시지 표시

### 사용 예시

```kotlin
// ViewModel에서 텍스트로부터 ChatGPT 응답 받기
viewModel.getChatGptResponse("안녕하세요, 오늘 날씨가 어떤가요?")
```

## STT 텍스트로부터 ChatGPT 응답 받기

STT로 변환된 텍스트를 기반으로 ChatGPT의 응답을 받는 기능을 구현하는 방법을 설명합니다.

### 구현 단계

1. **데이터 클래스 정의**

```kotlin
data class ChatGptSttRequest(val audioText: String)
```

2. **API 인터페이스 정의**

```kotlin
interface SaegilApiService {
    @POST("/api/v1/llm/chatgpt/stt")
    suspend fun chatGptFromStt(@Body request: ChatGptSttRequest): Response<ChatGptResponse>
}
```

3. **ViewModel 구현**

ViewModel에서는 다음 기능을 구현해야 합니다:

- STT로 변환된 텍스트를 기반으로 ChatGPT 응답을 받는 API 호출
- 응답으로 받은 텍스트 처리

4. **Compose UI 구현**

UI에서는 다음 요소를 포함해야 합니다:

- STT 텍스트 입력 필드 또는 STT 결과 표시
- 응답 받기 버튼
- 로딩 인디케이터
- ChatGPT 응답 표시
- 에러 메시지 표시

### 사용 예시

```kotlin
// ViewModel에서 STT 텍스트로부터 ChatGPT 응답 받기
viewModel.getChatGptResponse("오늘 날씨가 어떤가요?")
```

## 오디오 URL로부터 ChatGPT 응답 받기

오디오 URL을 기반으로 ChatGPT의 응답을 받는 기능을 구현하는 방법을 설명합니다.

### 구현 단계

1. **데이터 클래스 정의**

```kotlin
data class ChatGptAudioUrlRequest(val audioUrl: String)
```

2. **API 인터페이스 정의**

```kotlin
interface SaegilApiService {
    @POST("/api/v1/llm/chatgpt/audio-url")
    suspend fun chatGptFromAudioUrl(@Body request: ChatGptAudioUrlRequest): Response<ChatGptResponse>
}
```

3. **ViewModel 구현**

ViewModel에서는 다음 기능을 구현해야 합니다:

- 오디오 URL을 기반으로 ChatGPT 응답을 받는 API 호출
- 응답으로 받은 텍스트 처리

4. **Compose UI 구현**

UI에서는 다음 요소를 포함해야 합니다:

- 오디오 URL 입력 필드
- 응답 받기 버튼
- 로딩 인디케이터
- ChatGPT 응답 표시
- 에러 메시지 표시

### 사용 예시

```kotlin
// ViewModel에서 오디오 URL로부터 ChatGPT 응답 받기
viewModel.getChatGptResponseFromAudioUrl("https://example.com/audio/sample.mp3")
```

## 오디오 파일로부터 ChatGPT 응답 받기

안드로이드 기기에서 오디오 파일을 선택하여 ChatGPT의 응답을 받는 기능을 구현하는 방법을 설명합니다.

### 구현 단계

1. **API 인터페이스 정의**

```kotlin
interface SaegilApiService {
    @Multipart
    @POST("/api/v1/llm/chatgpt/upload")
    suspend fun chatGptFromFile(@Part file: MultipartBody.Part): Response<ChatGptResponse>
}
```

2. **ViewModel 구현**

ViewModel에서는 다음 기능을 구현해야 합니다:

- 선택한 오디오 파일을 임시 파일로 변환
- 멀티파트 요청 생성
- 파일에서 ChatGPT 응답을 받는 API 호출
- 응답으로 받은 텍스트 처리

3. **Compose UI 구현**

UI에서는 다음 요소를 포함해야 합니다:

- 파일 선택 버튼
- 선택된 파일 정보 표시
- 응답 받기 버튼
- 로딩 인디케이터
- ChatGPT 응답 표시
- 에러 메시지 표시

### 사용 예시

```kotlin
// 파일 선택기 설정
val audioPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
) { uri: Uri? ->
    if (uri != null) {
        viewModel.getChatGptResponseFromFile(context, uri)
    }
}

// 파일 선택기 실행
audioPickerLauncher.launch("audio/*")
```

## 에러 처리

API 호출 시 발생할 수 있는 에러를 처리하는 방법을 설명합니다.

### 일반적인 에러 처리 패턴

```kotlin
try {
    // API 호출
    val response = apiService.someEndpoint(request)

    if (response.isSuccessful && response.body() != null) {
        // 성공 처리
    } else {
        // HTTP 에러 처리
        val errorMessage = "서버 오류: ${response.code()}"
    }
} catch (e: Exception) {
    // 네트워크 오류 또는 기타 예외 처리
    val errorMessage = "네트워크 오류: ${e.message}"
}
```

### 에러 상태 표시

UI에서 에러 상태를 표시하는 방법:

```kotlin
when (val state = uiState) {
    // 다른 상태 처리...
    is ErrorState -> {
        Text(
            text = state.message,
            color = MaterialTheme.colorScheme.error
        )
    }
    else -> {
        // 다른 상태 처리
    }
}
```

## 전체 구현 흐름

안드로이드 앱에서 LlmProxyController를 사용하는 전체 구현 흐름을 설명합니다.

1. **프로젝트 설정**
    - 필요한 라이브러리 추가
    - 인터넷 권한 설정
    - 파일 접근 권한 설정

2. **API 인터페이스 및 데이터 클래스 정의**
    - 각 엔드포인트에 대한 API 인터페이스 정의
    - 요청 및 응답 데이터 클래스 정의

3. **Retrofit 클라이언트 설정**
    - OkHttpClient 구성
    - Retrofit 인스턴스 생성
    - API 서비스 인스턴스 생성

4. **ViewModel 구현**
    - 각 기능에 대한 ViewModel 구현
    - UI 상태 관리
    - API 호출 및 응답 처리
    - 에러 처리

5. **Compose UI 구현**
    - 각 기능에 대한 화면 구현
    - 사용자 입력 처리
    - 상태에 따른 UI 표시
    - 에러 메시지 표시

6. **권한 처리**
    - 인터넷 권한 확인
    - 파일 접근 권한 요청 및 확인

7. **테스트 및 디버깅**
    - 각 기능 테스트
    - 에러 케이스 테스트
    - 로그 확인

이 가이드를 따라 구현하면 안드로이드 앱에서 Saegil 서버의 LlmProxyController를 사용하여 텍스트-음성 변환, 음성-텍스트 변환, ChatGPT 응답 생성 등의 기능을 구현할 수 있습니다.

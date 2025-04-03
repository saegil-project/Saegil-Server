# OpenAI Controller Guide

## curl 명령어로 TextToSpeech API 요청하기

TextToSpeech API를 curl 명령어로 요청하는 방법은 다음과 같습니다:

```bash
curl -X POST \
  http://localhost:8080/api/v1/tts/stream \
  -H 'Content-Type: application/json' \
  -d '{"text":"안녕하세요, 반갑습니다. 새길팀 화이팅!! 이거 쉽지는 않겠지만 우리 한 번 열심히 해봐요!"}' \
  --output speech.mp3
```

### 명령어 설명

- `-X POST`: HTTP POST 메소드를 사용합니다.
- `http://localhost:8080/api/v1/tts/stream`: 요청할 API 엔드포인트 URL입니다.
- `-H 'Content-Type: application/json'`: 요청 헤더에 Content-Type을 JSON으로 지정합니다.
- `-d '{"text":"안녕하세요, 반갑습니다."}'`: JSON 형식의 요청 본문을 지정합니다. 한국어 텍스트를 포함할 수 있습니다.
- `--output speech.mp3`: 응답으로 받은 오디오 데이터를 speech.mp3 파일로 저장합니다.

### 주의사항

- 서버가 로컬에서 실행 중이어야 합니다 (포트 8080).
- 환경 변수 `OPENAI_API_KEY`가 설정되어 있어야 합니다 (application-local.yml 설정 참조).
- 응답은 MP3 형식의 오디오 파일로 저장됩니다.

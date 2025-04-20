# 새길 서버

## 프로젝트 개요

새길 서버는 사용자 인증, 공지사항 크롤링 및 조회, 지도 기반 기관 검색, LLM 프록시 기능 등을 제공하는 백엔드 애플리케이션입니다.

## 사용 스택

- **언어**: Java 21
- **프레임워크**: Spring Boot 3.4.4
- **데이터베이스**:
    - JPA (Hibernate)
    - MySQL (운영 환경)
    - H2 (로컬 개발 및 테스트 환경)
- **빌드 도구**: Gradle (Kotlin DSL)
- **인증**:
    - OAuth 2.0 (Kakao)
    - JWT (jjwt 라이브러리)
- **API 문서화**: Springdoc OpenAPI (Swagger UI)
- **웹 크롤링**: Jsoup
- **테스트**: JUnit 5, AssertJ
- **기타**: Lombok

## 주요 기능

- **사용자 인증**: Kakao OAuth 2.0을 이용한 소셜 로그인 및 JWT 기반의 토큰 인증 시스템 (로그인, 로그아웃, 토큰 재발급, 회원 탈퇴)
- **공지사항**: 남북하나재단, 통일부 웹사이트의 공지사항을 크롤링하여 저장하고, 검색 및 필터링 기능을 제공합니다.
- **지도**: 사용자 위치 기반으로 주변 기관(복지관, 주민센터 등) 정보를 조회하는 기능을 제공합니다.
- **LLM 프록시**: 별도의 LLM 서버(`Saegil-LLM-Server`)와 연동하여 다음과 같은 기능을 제공합니다.
    - Text-to-Speech (TTS)
    - Speech-to-Text (STT)
    - ChatGPT 연동
    - OpenAI Assistant API 연동 (텍스트/음성 입력 및 출력 지원)

## API 문서

애플리케이션 실행 후 `/swagger-ui.html` 경로에서 API 문서를 확인할 수 있습니다.

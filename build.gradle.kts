plugins {
    java
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
}

group = "com.newworld"
version = "0.0.1-SNAPSHOT"
val springAiVersion = "1.0.0-M6"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    // Spring AI OpenAI Starter
    implementation("org.springframework.ai:spring-ai-openai-spring-boot-starter")

    // Spring Boot Starters
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Swagger / OpenAPI for WebFlux
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.2.0")

    // Lombok for boilerplate code reduction
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Testing dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // MySQL 커넥터 의존성 추가
    runtimeOnly("com.mysql:mysql-connector-j:8.2.0")

    // H2 데이터베이스 의존성 추가
    runtimeOnly("com.h2database:h2:2.2.220")

    // AssertJ 의존성 추가
    testImplementation("org.assertj:assertj-core:4.0.0-M1")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.ai:spring-ai-bom:$springAiVersion")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

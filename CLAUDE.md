# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```powershell
# Build
./gradlew build

# Run application (default: http://localhost:8080)
./gradlew bootRun

# Run all tests
./gradlew test

# Run a single test class
./gradlew test --tests "kr.it.rudy.admin.AdminSpringApplicationTests"

# Clean build
./gradlew clean build

# Package JAR
./gradlew bootJar
```

## Tech Stack

- **Java 21**, **Spring Boot 4.0.6**, **Gradle 9.5.1**
- **Web**: Spring MVC + Thymeleaf (server-side rendered UI)
- **Security**: Spring Security + OAuth2 Resource Server
- **Database**: H2 (dev/test), PostgreSQL (production)
- **Observability**: Spring Actuator + Micrometer/Prometheus
- **Utilities**: Lombok, Bean Validation

## Architecture

Spring Boot MVC admin application under `kr.it.rudy.admin`. Domain-first package structure:

```
kr.it.rudy.admin/
├── common/           공유 코드 (exception, filter, response, util, config)
└── {domain}/
    ├── domain/       Entity, Repository 인터페이스, Enum
    ├── application/  Service
    ├── web/          @Controller — Thymeleaf 뷰 반환
    ├── api/          @RestController — JSON 응답
    └── dto/          Request / Response 레코드
```

- `web/` and `api/` are always separate — `@Controller` goes in `web/`, `@RestController` goes in `api/`
- Exception handling is split: `ApiExceptionHandler` (`@RestControllerAdvice`) for REST, `ViewExceptionHandler` (`@ControllerAdvice`) for Thymeleaf
- Security is OAuth2 resource-server based — accepts tokens from an external authorization server
- Actuator + Prometheus scrape endpoint at `/actuator/prometheus`

## History

`.claude/history.md` — 구현을 진행하면서 트러블슈팅, 주요 결정, 변경 이력을 날짜 역순으로 누적 기록한다. 항목 추가 시 파일 내 템플릿을 사용한다.

## Conventions

Detailed coding rules are in `.claude/rules/`:

| File | 내용 |
|------|------|
| `common-conventions.md` | Lombok 사용 규칙, 유틸리티 클래스, 상수 |
| `controller-conventions.md` | `@RestController` / `@Controller` 구조 |
| `directory-conventions.md` | 패키지·디렉토리 구조, templates/ 위치 |
| `exception-conventions.md` | ErrorCode, 예외 핸들러 분리 구조, 응답 형식 |
| `logging-conventions.md` | 로그 태그 형식, 레벨 기준, MDC |
| `naming-conventions.md` | 클래스·인터페이스·구현체 네이밍 |
| `test-conventions.md` | 테스트 어노테이션 선택, Mock 규칙, 네이밍 |

## Configuration

Profile-based config under `src/main/resources/`:

| File | Purpose |
|------|---------|
| `application.yaml` | Common settings: app name, `server.port: 8080`, default profile (`local`) |
| `application-local.yaml` | H2 in-memory DB, H2 console at `/h2-console`, `ddl-auto: create-drop`, DEBUG logging |
| `application-prod.yaml` | PostgreSQL via `${DB_USERNAME}` / `${DB_PASSWORD}` env vars, `ddl-auto: validate`, INFO logging |

To run with prod profile: `./gradlew bootRun --args='--spring.profiles.active=prod'`

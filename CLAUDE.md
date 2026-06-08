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

The project is structured as a traditional Spring Boot MVC admin application under `kr.it.rudy.admin`:

- `AdminSpringApplication` — entry point (`@SpringBootApplication`)
- Expected layering: Controllers → Services → Repositories, with Thymeleaf templates in `src/main/resources/templates/`
- Security is OAuth2 resource-server based — the app accepts tokens from an external authorization server rather than issuing them
- Dual database setup: H2 is available for local/test, PostgreSQL for production (configure via profiles)
- Actuator endpoints are included; Prometheus scrape endpoint will be at `/actuator/prometheus`

## Configuration

Profile-based config under `src/main/resources/`:

| File | Purpose |
|------|---------|
| `application.yaml` | Common settings: app name, `server.port: 8080`, default profile (`local`) |
| `application-local.yaml` | H2 in-memory DB, H2 console at `/h2-console`, `ddl-auto: create-drop`, DEBUG logging |
| `application-prod.yaml` | PostgreSQL via `${DB_USERNAME}` / `${DB_PASSWORD}` env vars, `ddl-auto: validate`, INFO logging |

To run with prod profile: `./gradlew bootRun --args='--spring.profiles.active=prod'`

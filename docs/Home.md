# Pessoa API - Wiki

**Pessoa API** is a Spring Boot REST service for managing person records (pessoas). It provides CRUD operations, age calculation, and salary projection based on admission date and configurable salary parameters.

## Tech Stack

| Component | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.0.5 |
| Build Tool | Maven (with Maven Wrapper) |
| API Docs | SpringDoc OpenAPI (Swagger UI) |
| Security | Spring Security (all endpoints open) |
| Persistence | In-memory (`ConcurrentHashMap`) |
| Observability | OpenTelemetry (metrics export disabled by default) |
| Locale | pt_BR (Brazilian Portuguese) |

## Quick Start

```bash
# Build
./mvnw clean package

# Run
java -jar target/pessoa-1.0.0.jar

# Or via Maven
./mvnw spring-boot:run
```

The application starts on the default port `8080` with the `dev` profile active.

## Key URLs

| URL | Description |
|---|---|
| `http://localhost:8080/person` | Person API base |
| `http://localhost:8080/swagger-ui.html` | Swagger UI |
| `http://localhost:8080/api-docs` | OpenAPI JSON spec |

## Wiki Pages

- [Architecture](Architecture.md) - Hexagonal architecture and package structure
- [Domain Model](Domain-Model.md) - The `Pessoa` entity and business rules
- [API Reference](API-Reference.md) - All REST endpoints with examples
- [Salary Calculation](Salary-Calculation.md) - How salary projection works
- [Configuration](Configuration.md) - Application properties and salary parameters
- [Error Handling](Error-Handling.md) - Exception hierarchy and RFC 9457 responses
- [Infrastructure](Infrastructure.md) - Rate limiting, Jackson serialization, security

# Infrastructure

## Rate Limiting

The application includes a token-bucket rate limiter applied to all requests.

### Configuration

| Parameter | Value |
|---|---|
| Capacity | 10 tokens |
| Refill | 10 tokens per 60 seconds |
| Key | Authenticated user name, or remote IP address as fallback |

### Behavior

- Each request consumes one token
- When tokens are exhausted, the server responds with HTTP `429 Too Many Requests`
- Tokens refill in bulk (not per-second) after the refill interval elapses

### 429 Response

```json
{
  "title": "Too Many Requests",
  "status": 429,
  "detail": "Rate limit exceeded"
}
```

### Implementation

- **`RateLimitFilter`**: A `OncePerRequestFilter` that intercepts every request, resolves a key (user or IP), and delegates to a per-key `SimpleRateLimiter`.
- **`SimpleRateLimiter`**: A synchronized token-bucket implementation using `AtomicInteger` for token count and `volatile long` for last refill time.

## Security

Spring Security is configured in `SecurityConfig` with:

- **CSRF disabled** (standard for stateless REST APIs)
- **All requests permitted** (no authentication required)

This is suitable for the current prototype/drill scope. For production use, authentication and authorization should be added.

## Jackson Serialization

### Date Formatting

Dates (`LocalDate`) are serialized using `dd/MM/yyyy` format with the `America/Sao_Paulo` timezone, configured via `@JsonFormat` annotations on the `Pessoa` entity.

Global Jackson settings in `application.yml`:
- `write-dates-as-timestamps: false` - Dates as strings, not epoch numbers
- `locale: pt_BR` - Brazilian Portuguese locale
- `time-zone: America/Sao_Paulo` - Sao Paulo timezone

### BigDecimal Formatting

A custom `BigDecimalPtBrSerializer` formats `BigDecimal` values using pt-BR locale conventions:
- Comma (`,`) as decimal separator
- Period (`.`) as thousands separator

This serializer is registered globally via `JacksonConfig` and applies to all `BigDecimal` fields in JSON responses.

## Enum Converters

`WebConfig` registers Spring `Converter` beans for `SalaryOutput` and `AgeOutput` enums, enabling case-insensitive query parameter parsing (e.g., `?output=full` works the same as `?output=FULL`).

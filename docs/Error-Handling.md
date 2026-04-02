# Error Handling

All error responses follow the **RFC 9457 Problem Details** format via Spring's `ProblemDetail` class.

## Response Format

```json
{
  "type": "about:blank",
  "title": "Resource not found",
  "status": 404,
  "detail": "Pessoa nao encontrada",
  "instance": "/person/99",
  "timestamp": "2026-04-02T12:00:00Z"
}
```

Every error response includes a `timestamp` property added by the `GlobalExceptionHandler.enrich()` method.

## Exception Hierarchy

| Exception | HTTP Status | Title | When |
|---|---|---|---|
| `PessoaNotFoundException` | 404 Not Found | Resource not found | Person ID does not exist |
| `ConflictException` | 409 Conflict | Conflict | Duplicate ID or name |
| `DomainException` | 422 Unprocessable Entity | Business rule violation | e.g., admission date before birth date |
| `MethodArgumentNotValidException` | 400 Bad Request | Validation failed | Bean Validation constraint violations |
| `MethodArgumentTypeMismatchException` | 400 Bad Request | Invalid parameter | Invalid enum value for query params |
| `Exception` (fallback) | 500 Internal Server Error | Internal server error | Any unhandled exception |

## Validation Errors (400)

Validation errors include a structured `errors` array with per-field details:

```json
{
  "type": "about:blank",
  "title": "Validation failed",
  "status": 400,
  "detail": "nome: Nome e obrigatorio, dataNascimento: Data de nascimento e obrigatoria",
  "instance": "/person",
  "errors": [
    { "field": "nome", "message": "Nome e obrigatorio" },
    { "field": "dataNascimento", "message": "Data de nascimento e obrigatoria" }
  ],
  "timestamp": "2026-04-02T12:00:00Z"
}
```

## Enum Mismatch Errors (400)

When an invalid enum value is passed (e.g., `?output=INVALID`), the response includes the allowed values:

```json
{
  "type": "about:blank",
  "title": "Invalid parameter",
  "status": 400,
  "detail": "Invalid value 'INVALID' for parameter 'output'. Allowed: [DAYS, MONTHS, YEARS]",
  "instance": "/person/1/age",
  "parameter": "output",
  "invalidValue": "INVALID",
  "allowedValues": ["DAYS", "MONTHS", "YEARS"],
  "timestamp": "2026-04-02T12:00:00Z"
}
```

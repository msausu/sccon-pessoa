# Configuration

## Application Properties

The application is configured via `src/main/resources/application.yml`.

### Spring Settings

```yaml
spring:
  application:
    name: pessoa
  profiles:
    active: dev              # Activates seed data loading
  web:
    locale: pt_BR
    locale-resolver: fixed
  jackson:
    datatype:
      datetime:
        write-dates-as-timestamps: false
    locale: pt_BR
    time-zone: America/Sao_Paulo
```

### OpenTelemetry

```yaml
management:
  otlp:
    metrics:
      export:
        enabled: false       # OTLP metrics disabled by default
```

### Swagger / OpenAPI

```yaml
springdoc:
  api-docs:
    path: /api-docs          # OpenAPI spec endpoint
  swagger-ui:
    path: /swagger-ui.html   # Interactive Swagger UI
```

### Salary Parameters

```yaml
salario:
  base: 1558.00              # Starting salary (BRL)
  minimo:
    referencia: 1302.00      # Minimum wage reference for MIN output
  taxa: 1.18                 # Annual multiplier (1.18 = 18% raise)
  bonus: 500.00              # Flat annual bonus (BRL)
```

These properties are bound to the `SalarioProperties` class via `@ConfigurationProperties(prefix = "salario")`.

#### Validation

On startup (`@PostConstruct`), `SalarioProperties` validates and resets invalid values to defaults:

| Property | Validation | Default |
|---|---|---|
| `base` | Must not be null; `taxa` must be > 0 (guards both) | 1558.00 |
| `minimoReferencia` | Must be > 0 | 1302.00 |
| `taxa` | Must be > 0 | 1.18 |
| `bonus` | Must be >= 0 | 500.00 |

## Profiles

| Profile | Behavior |
|---|---|
| `dev` | Loads seed data via `DataInitializer` (3 sample persons) |
| (default) | No seed data |

## Clock

The system clock is injected as a Spring bean (`ClockConfig`), returning `Clock.systemDefaultZone()`. This enables deterministic testing by substituting a fixed clock in test configurations.

# Architecture

The project follows **Hexagonal Architecture** (Ports & Adapters), cleanly separating domain logic from infrastructure concerns.

## Package Structure

```
com.sccon.geo.pessoa
├── PessoaApplication.java              # Spring Boot entry point
│
├── domain/                             # Core business logic (no framework deps)
│   ├── model/
│   │   └── Pessoa.java                 # The aggregate root entity
│   ├── port/
│   │   ├── in/                         # Inbound ports (use cases)
│   │   │   ├── AdmitirPessoaUseCase
│   │   │   ├── AtualizarPessoaUseCase
│   │   │   ├── BuscarPessoaUseCase
│   │   │   ├── ConsultarIdadeUseCase
│   │   │   ├── ConsultarSalarioUseCase
│   │   │   └── RemoverPessoaUseCase
│   │   └── out/                        # Outbound ports (repositories)
│   │       └── PessoaRepositoryPort
│   └── service/                        # Domain services
│       ├── AgeCalculator
│       └── SalaryCalculator
│
├── application/                        # Application layer (orchestration)
│   ├── command/                        # Input DTOs (commands)
│   │   ├── AdmitirPessoaCommand
│   │   ├── AtualizarPessoaCommand
│   │   └── PatchPessoaCommand
│   ├── result/                         # Output DTOs
│   │   ├── AgeOutput (enum)
│   │   ├── AgeResult (record)
│   │   ├── SalaryOutput (enum)
│   │   └── SalaryResult (record)
│   └── service/                        # Use case implementations
│       ├── PessoaApplicationService
│       ├── ConsultarIdadeService
│       └── ConsultarSalarioService
│
├── adapter/                            # Adapters (infrastructure)
│   ├── in/web/                         # Inbound adapter: REST API
│   │   ├── PessoaController
│   │   ├── dto/
│   │   │   └── SalaryResponse
│   │   └── config/
│   │       ├── SecurityConfig
│   │       ├── WebConfig
│   │       ├── RateLimitFilter
│   │       └── SimpleRateLimiter
│   └── out/persistence/               # Outbound adapter: storage
│       └── InMemoryPessoaRepository
│
├── config/                             # Cross-cutting configuration
│   ├── AppConfig
│   ├── ClockConfig
│   ├── DataInitializer
│   └── SalarioProperties
│
├── exception/                          # Exception hierarchy
│   ├── GlobalExceptionHandler
│   ├── PessoaNotFoundException
│   ├── ConflictException
│   └── DomainException
│
└── infrastructure/jackson/             # Serialization customizations
    ├── JacksonConfig
    └── BigDecimalPtBrSerializer
```

## Dependency Flow

```
Controller (adapter.in) ──> Use Case Ports (domain.port.in)
                                    │
                            Application Services
                                    │
                            Domain Services + Model
                                    │
                          Repository Port (domain.port.out)
                                    │
                        InMemoryPessoaRepository (adapter.out)
```

The domain layer has **zero framework dependencies**. All Spring annotations live in the adapter and application layers.

## Key Design Decisions

1. **In-memory storage**: Uses `ConcurrentHashMap` with `AtomicLong` ID generation -- suitable for prototyping/drill exercises. Easily swappable via `PessoaRepositoryPort`.
2. **Clock injection**: `Clock` is injected as a Spring bean for testability of date-based calculations.
3. **Command/Result separation**: Input commands (validated with Jakarta Bean Validation) are distinct from output result records.
4. **Single service, multiple interfaces**: `PessoaApplicationService` implements four use case interfaces (`Admitir`, `Atualizar`, `Buscar`, `Remover`), keeping CRUD cohesive. Age and salary queries are separate services.

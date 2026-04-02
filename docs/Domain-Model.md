# Domain Model

## Pessoa (Person)

The central entity representing a person/employee.

### Fields

| Field | Type | JSON Format | Description |
|---|---|---|---|
| `id` | `Long` | number | Auto-generated unique identifier |
| `nome` | `String` | string | Full name (2-100 characters) |
| `dataNascimento` | `LocalDate` | `"dd/MM/yyyy"` | Date of birth (must be in the past) |
| `dataAdmissao` | `LocalDate` | `"dd/MM/yyyy"` | Admission/hire date |

### Example JSON

```json
{
  "id": 1,
  "nome": "Jose da Silva",
  "dataNascimento": "06/04/2000",
  "dataAdmissao": "10/05/2020"
}
```

### Business Rules

1. **Date validation**: `dataAdmissao` cannot be before `dataNascimento`. Violating this throws a `DomainException` (HTTP 422).
2. **Uniqueness by ID**: Creating a person with an existing ID throws a `ConflictException` (HTTP 409).
3. **Uniqueness by name**: Creating or updating a person with a name that already exists throws a `ConflictException` (HTTP 409).
4. **ID constraints**: If provided, ID must be greater than zero.
5. **Partial updates (PATCH)**: Only non-null fields in the command are applied. Null fields retain their current values.

### Equality

Two `Pessoa` instances are considered equal if they share the same `nome`, `dataNascimento`, and `dataAdmissao`. The `id` field is **not** part of equality -- this is an intentional domain choice.

## Seed Data (dev profile)

When running with the `dev` profile, three persons are pre-loaded:

| ID | Nome | Data Nascimento | Data Admissao |
|---|---|---|---|
| 1 | Jose da Silva | 2000-04-06 | 2020-05-10 |
| 2 | Ana Maria | 1985-05-10 | 2018-03-15 |
| 3 | Carlos Alberto | 1992-07-20 | 2021-06-01 |

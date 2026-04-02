# API Reference

Base URL: `http://localhost:8080`

All endpoints are under `/person`. Dates use `dd/MM/yyyy` format (Brazilian standard).

---

## Create Person

```
POST /person
Content-Type: application/json
```

### Request Body

| Field | Type | Required | Validation |
|---|---|---|---|
| `id` | Long | No | If provided, must be > 0 and not already exist |
| `nome` | String | Yes | 2-100 characters, must not already exist |
| `dataNascimento` | String | Yes | `dd/MM/yyyy`, must be in the past |
| `dataAdmissao` | String | Yes | Cannot be before `dataNascimento` |

### Example

```bash
curl -X POST http://localhost:8080/person \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Maria Santos",
    "dataNascimento": "15/03/1990",
    "dataAdmissao": "01/01/2022"
  }'
```

### Responses

- `200 OK` - Returns the created `Pessoa` (with generated ID if none was provided)
- `400 Bad Request` - Validation errors
- `409 Conflict` - ID or name already exists

---

## Update Person (Full)

```
PUT /person/{id}
Content-Type: application/json
```

Replaces `nome` and `dataNascimento`. The `dataAdmissao` is **not** updatable via PUT.

### Request Body

| Field | Type | Required | Validation |
|---|---|---|---|
| `nome` | String | Yes | 2-100 characters, must not already exist |
| `dataNascimento` | String | Yes | `dd/MM/yyyy`, must be in the past |

### Example

```bash
curl -X PUT http://localhost:8080/person/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Jose Silva Atualizado",
    "dataNascimento": "06/04/2000"
  }'
```

### Responses

- `200 OK` - Returns the updated `Pessoa`
- `404 Not Found` - Person not found
- `409 Conflict` - Name already exists
- `422 Unprocessable Entity` - Business rule violation

---

## Patch Person (Partial)

```
PATCH /person/{id}
Content-Type: application/json
```

Only non-null fields are applied.

### Request Body

| Field | Type | Required |
|---|---|---|
| `nome` | String | No |
| `dataNascimento` | String | No |
| `dataAdmissao` | String | No |

### Example

```bash
curl -X PATCH http://localhost:8080/person/1 \
  -H "Content-Type: application/json" \
  -d '{"nome": "Jose da Silva Junior"}'
```

### Responses

- `200 OK` - Returns the updated `Pessoa`
- `404 Not Found` - Person not found
- `409 Conflict` - Name already exists
- `422 Unprocessable Entity` - Business rule violation (e.g., admission before birth)

---

## List All Persons

```
GET /person
```

Returns all persons sorted alphabetically by `nome`.

### Example

```bash
curl http://localhost:8080/person
```

### Response

```json
[
  {
    "id": 2,
    "nome": "Ana Maria",
    "dataNascimento": "10/05/1985",
    "dataAdmissao": "15/03/2018"
  },
  {
    "id": 3,
    "nome": "Carlos Alberto",
    "dataNascimento": "20/07/1992",
    "dataAdmissao": "01/06/2021"
  }
]
```

---

## Get Person by ID

```
GET /person/{id}
```

### Example

```bash
curl http://localhost:8080/person/1
```

### Responses

- `200 OK` - Returns the `Pessoa`
- `404 Not Found` - Person not found

---

## Delete Person

```
DELETE /person/{id}
```

### Example

```bash
curl -X DELETE http://localhost:8080/person/1
```

### Responses

- `204 No Content` - Successfully deleted
- `404 Not Found` - Person not found

---

## Get Age

```
GET /person/{id}/age
```

Calculates the person's current age based on `dataNascimento`.

### Query Parameters

| Parameter | Type | Default | Values |
|---|---|---|---|
| `output` | String | `DAYS` | `DAYS`, `MONTHS`, `YEARS` |

### Content Negotiation

- `Accept: application/json` - Returns structured JSON
- `Accept: text/plain` - Returns just the numeric value

### Examples

```bash
# JSON response
curl -H "Accept: application/json" \
  "http://localhost:8080/person/1/age?output=YEARS"

# Response:
# {"id": 1, "value": 25, "output": "YEARS"}

# Plain text response
curl -H "Accept: text/plain" \
  "http://localhost:8080/person/1/age?output=DAYS"

# Response:
# 9493
```

---

## Get Salary

```
GET /person/{id}/salary
```

Projects the person's current salary based on admission date, using configurable base, rate, and bonus parameters.

### Query Parameters

| Parameter | Type | Default | Values |
|---|---|---|---|
| `output` | String | `FULL` | `FULL` (absolute BRL), `MIN` (in minimum wages) |

### Content Negotiation

- `Accept: application/json` - Returns structured JSON
- `Accept: text/plain` - Returns formatted value (pt-BR locale)

### Examples

```bash
# JSON response (absolute salary)
curl -H "Accept: application/json" \
  "http://localhost:8080/person/1/salary?output=FULL"

# Response:
# {"pessoaId": 1, "tipo": "FULL", "valor": 5432.10}

# Plain text response (in minimum wages)
curl -H "Accept: text/plain" \
  "http://localhost:8080/person/1/salary?output=MIN"

# Response:
# 4,17
```

# Salary Calculation

The salary projection system estimates an employee's current salary based on their admission date and configurable parameters.

## Formula

The salary is calculated iteratively for each full year of employment:

```
salary = base
for each year of employment:
    salary = salary * taxa + bonus
```

Where:
- **base** = starting salary (default: R$ 1.558,00)
- **taxa** = annual multiplier/rate (default: 1.18, i.e., 18% raise per year)
- **bonus** = flat annual bonus added after the rate (default: R$ 500,00)

## Example

For a person admitted on 2020-05-10, calculated on 2026-04-02 (5 full years):

```
Year 0: salary = 1558.00
Year 1: salary = 1558.00 * 1.18 + 500.00 = 2338.44
Year 2: salary = 2338.44 * 1.18 + 500.00 = 3259.36
Year 3: salary = 3259.36 * 1.18 + 500.00 = 4346.04
Year 4: salary = 4346.04 * 1.18 + 500.00 = 5628.33
Year 5: salary = 5628.33 * 1.18 + 500.00 = 7141.43
```

## Output Modes

| Mode | Description | Example |
|---|---|---|
| `FULL` | Absolute salary in BRL (2 decimal places, ceiling rounding) | `7141.43` |
| `MIN` | Salary divided by minimum wage reference (2 decimal places) | `5.49` |

## Minimum Wage Reference

The `minimoReferencia` parameter (default: R$ 1.302,00) is used only when `output=MIN`. The salary is divided by this value to express the result in multiples of the minimum wage.

## Configuration

All parameters are configurable via `application.yml` under the `salario` prefix. See [Configuration](Configuration.md) for details.

## Implementation

- **`SalaryCalculator`** (domain service): Pure calculation logic with no framework dependencies.
- **`ConsultarSalarioService`** (application service): Orchestrates lookup, calculation, and output formatting.
- **`SalaryResponse`** (web DTO): Formats the plain-text output using `DecimalFormat` with pt-BR locale (comma as decimal separator, period as thousands separator).

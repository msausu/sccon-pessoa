package com.sccon.geo.pessoa.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class SalaryCalculationTest {

    private SalaryCalculator calculator;
    private final BigDecimal base = new BigDecimal("1558.00");
    private final BigDecimal taxa = new BigDecimal("1.18");
    private final BigDecimal bonus = new BigDecimal("500.00");

    @BeforeEach
    void setUp() {
        calculator = new SalaryCalculator(base, taxa, bonus);
    }

    @Test
    @DisplayName("Should return base salary when years of service is zero")
    void shouldReturnBaseSalaryWhenYearsIsZero() {
        LocalDate admissao = LocalDate.of(2023, 2, 7);
        LocalDate hoje = LocalDate.of(2023, 2, 7);

        BigDecimal resultado = calculator.calcular(admissao, hoje);

        assertThat(resultado).isEqualByComparingTo(base);
    }

    @ParameterizedTest(name = "Years: {0}, Expected: {1}")
    @CsvSource({
            "1, 2338.44",        // 1558 * 1.18 + 500
            "2, 3259.3592",      // 2338.44 * 1.18 + 500
            "3, 4346.043856"     // 3259.3592 * 1.18 + 500
    })
    @DisplayName("Should calculate compound growth for multiple years")
    void shouldCalculateCompoundGrowth(int years, String expected) {
        LocalDate admissao = LocalDate.of(2020, 1, 1);
        LocalDate hoje = admissao.plusYears(years);

        BigDecimal resultado = calculator.calcular(admissao, hoje);

        assertThat(resultado).isEqualByComparingTo(expected);
    }

    @Test
    @DisplayName("Should handle leap year correctly")
    void shouldHandleLeapYear() {
        LocalDate admissao = LocalDate.of(2024, 2, 29);
        
        // 2024-02-29 to 2025-02-28 is 11 months and 28 or 29 days (0 years)
        LocalDate oneDayBeforeAnniversary = LocalDate.of(2025, 2, 28);
        assertThat(calculator.calcular(admissao, oneDayBeforeAnniversary))
                .as("Less than a year from leap day")
                .isEqualByComparingTo(base);

        // 2024-02-29 to 2025-03-01 is 1 year and 1 day
        LocalDate oneYearAndOneDayLater = LocalDate.of(2025, 3, 1);
        assertThat(calculator.calcular(admissao, oneYearAndOneDayLater))
                .as("One year from leap day (non-leap year target)")
                .isEqualByComparingTo("2338.44");

        // Leap to leap (exactly 4 years)
        LocalDate leapToLeap = LocalDate.of(2028, 2, 29);
        // 4 iterations:
        // 1: 2338.44
        // 2: 3259.3592
        // 3: 4346.043856
        // 4: 5628.33175008
        assertThat(calculator.calcular(admissao, leapToLeap))
                .as("Four years leap to leap")
                .isEqualByComparingTo("5628.33175008");
    }

    @Test
    @DisplayName("Should return base salary if today is before admission date")
    void shouldReturnBaseSalaryIfTodayIsBeforeAdmission() {
        LocalDate admissao = LocalDate.of(2023, 2, 7);
        LocalDate hoje = LocalDate.of(2022, 2, 7);

        BigDecimal resultado = calculator.calcular(admissao, hoje);

        assertThat(resultado).isEqualByComparingTo(base);
    }
}
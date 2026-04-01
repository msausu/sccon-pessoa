package com.sccon.geo.pessoa.application.service;

import com.sccon.geo.pessoa.application.result.SalaryOutput;
import com.sccon.geo.pessoa.application.result.SalaryResult;
import com.sccon.geo.pessoa.config.SalarioProperties;
import com.sccon.geo.pessoa.domain.model.Pessoa;
import com.sccon.geo.pessoa.domain.port.out.PessoaRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ConsultarSalarioServiceTest {

    private PessoaRepositoryPort repository;
    private ConsultarSalarioService service;

    private final LocalDate HOJE = LocalDate.of(2023, 2, 7);

    @BeforeEach
    void setup() {
        repository = mock(PessoaRepositoryPort.class);

        SalarioProperties props = new SalarioProperties();
        props.setBase(new BigDecimal("1558.00"));
        props.setMinimoReferencia(new BigDecimal("1302.00"));
        props.setTaxa(new BigDecimal("1.18"));
        props.setBonus(new BigDecimal("500.00"));

        Clock fixedClock = Clock.fixed(
                HOJE.atStartOfDay(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
        );

        service = new ConsultarSalarioService(repository, props, fixedClock);
    }

    static Stream<TestCase> salaryCases() {
        return Stream.of(
                // TWO_YEARS: 3259.3592. Service scales it to 2 decimals using CEILING -> 3259.36
                new TestCase("TWO_YEARS", LocalDate.of(2021, 2, 7), new BigDecimal("3259.36")),
                
                // ONE_YEAR: 2338.44ß
                new TestCase("ONE_YEAR", LocalDate.of(2022, 2, 7), new BigDecimal("2338.44")),
                
                // LESS_THAN_YEAR: 1558.00
                new TestCase("LESS_THAN_YEAR", LocalDate.of(2022, 2, 8), new BigDecimal("1558.00")),
                
                // EQUAL: 1558.00
                new TestCase("EQUAL", LocalDate.of(2023, 2, 7), new BigDecimal("1558.00")),
                
                // MINIMUM_SALARY: 1558 / 1302 = 1.1966... -> CEILING(2) = 1.20
                new TestCase("MINIMUM_SALARY", LocalDate.of(2023, 2, 7), new BigDecimal("1.20"), SalaryOutput.MIN)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("salaryCases")
    @DisplayName("Should calculate salary correctly for various admission dates")
    void shouldCalculateSalary(TestCase tc) {
        Pessoa pessoa = new Pessoa(
                1L,
                "João",
                LocalDate.of(1990, 1, 1),
                tc.admissao
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(pessoa));

        SalaryResult result = service.consultar(1L, tc.output);

        assertThat(result.valor())
                .as("Case: " + tc.name)
                .isEqualByComparingTo(tc.expected);
    }

    static class TestCase {
        String name;
        LocalDate admissao;
        BigDecimal expected;
        SalaryOutput output;

        TestCase(String name, LocalDate admissao, BigDecimal expected) {
            this(name, admissao, expected, SalaryOutput.FULL);
        }

        TestCase(String name, LocalDate admissao, BigDecimal expected, SalaryOutput output) {
            this.name = name;
            this.admissao = admissao;
            this.expected = expected;
            this.output = output;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
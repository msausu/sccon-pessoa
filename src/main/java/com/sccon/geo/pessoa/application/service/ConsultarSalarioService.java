package com.sccon.geo.pessoa.application.service;

import com.sccon.geo.pessoa.application.result.SalaryOutput;
import com.sccon.geo.pessoa.application.result.SalaryResult;
import com.sccon.geo.pessoa.config.SalarioProperties;
import com.sccon.geo.pessoa.domain.model.Pessoa;
import com.sccon.geo.pessoa.domain.port.in.ConsultarSalarioUseCase;
import com.sccon.geo.pessoa.domain.port.out.PessoaRepositoryPort;
import com.sccon.geo.pessoa.domain.service.SalaryCalculator;
import com.sccon.geo.pessoa.exception.PessoaNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;

@Service
public class ConsultarSalarioService implements ConsultarSalarioUseCase {

    private final PessoaRepositoryPort repository;
    private final SalaryCalculator calculator;
    private final SalarioProperties props;
    private final Clock clock;

    public ConsultarSalarioService(
            PessoaRepositoryPort repository,
            SalarioProperties props,
            Clock clock) {
        this.props = props;
        this.repository = repository;
        this.clock = clock;
        this.calculator = new SalaryCalculator(
                props.getBase(),
                props.getTaxa(),
                props.getBonus()
        );
    }

    @Override
    public SalaryResult consultar(Long id, SalaryOutput output) {

        Pessoa pessoa = repository.findById(id)
                .orElseThrow(() -> new PessoaNotFoundException("Pessoa não encontrada"));

        BigDecimal salario = calculator.calcular(
                pessoa.getDataAdmissao(),
                LocalDate.now(clock)
        );

        if (output == SalaryOutput.MIN) {
            salario = salario
                    .divide(props.getMinimoReferencia(), 10, RoundingMode.HALF_UP);
        }

        salario = salario.setScale(2, RoundingMode.CEILING);

        return new SalaryResult(id, salario, output);
    }
}

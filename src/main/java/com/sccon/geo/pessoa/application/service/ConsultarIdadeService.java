package com.sccon.geo.pessoa.application.service;

import com.sccon.geo.pessoa.application.result.AgeOutput;
import com.sccon.geo.pessoa.application.result.AgeResult;
import com.sccon.geo.pessoa.domain.model.Pessoa;
import com.sccon.geo.pessoa.domain.port.in.ConsultarIdadeUseCase;
import com.sccon.geo.pessoa.domain.port.out.PessoaRepositoryPort;
import com.sccon.geo.pessoa.domain.service.AgeCalculator;
import com.sccon.geo.pessoa.exception.PessoaNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;

@Service
public class ConsultarIdadeService implements ConsultarIdadeUseCase {

    private final PessoaRepositoryPort repository;
    private final Clock clock;
    private final AgeCalculator calculator = new AgeCalculator();

    public ConsultarIdadeService(PessoaRepositoryPort repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Override
    public AgeResult consultar(Long id, AgeOutput output) {

        Pessoa pessoa = repository.findById(id)
                .orElseThrow(() -> new PessoaNotFoundException("Pessoa não encontrada"));

        long idade = calculator.calcular(
                pessoa.getDataNascimento(),
                LocalDate.now(clock),
                output
        );

        return new AgeResult(id, idade, output);
    }
}
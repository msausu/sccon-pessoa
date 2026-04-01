package com.sccon.geo.pessoa.config;

import com.sccon.geo.pessoa.application.command.AdmitirPessoaCommand;
import com.sccon.geo.pessoa.domain.port.in.AdmitirPessoaUseCase;
import com.sccon.geo.pessoa.domain.port.in.BuscarPessoaUseCase;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;

@Configuration
@Profile("dev")
public class DataInitializer {

    @Bean
    CommandLineRunner init(
            AdmitirPessoaUseCase admitir,
            BuscarPessoaUseCase buscar // used for idempotency check
    ) {
        return args -> {

            if (buscar.existsAny()) {
                return;
            }

            admitir.admitir(new AdmitirPessoaCommand(
                    1L,
                    "José da Silva",
                    LocalDate.of(2000, 4, 6),
                    LocalDate.of(2020, 5, 10)
            ));

            admitir.admitir(new AdmitirPessoaCommand(
                    2L,
                    "Ana Maria",
                    LocalDate.of(1985, 5, 10),
                    LocalDate.of(2018, 3, 15)
            ));

            admitir.admitir(new AdmitirPessoaCommand(
                    3L,
                    "Carlos Alberto",
                    LocalDate.of(1992, 7, 20),
                    LocalDate.of(2021, 6, 1)
            ));
        };
    }
}
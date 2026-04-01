package com.sccon.geo.pessoa.application.command;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record AdmitirPessoaCommand(

        Long id,

        @NotBlank
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String nome,

        @NotNull @Past
        @NotNull(message = "Data de nascimento é obrigatória")
        @Past(message = "Data de nascimento deve estar no passado")
        LocalDate dataNascimento,

        @NotNull(message = "Data de nascimento é obrigatória")
        LocalDate dataAdmissao
) {}
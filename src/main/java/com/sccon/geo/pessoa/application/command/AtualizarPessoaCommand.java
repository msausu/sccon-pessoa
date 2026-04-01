package com.sccon.geo.pessoa.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AtualizarPessoaCommand(

        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String nome,

        @NotNull(message = "Data de nascimento é obrigatória")
        @Past(message = "Data de nascimento deve estar no passado")
        LocalDate dataNascimento
) {}
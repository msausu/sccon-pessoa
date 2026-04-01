package com.sccon.geo.pessoa.application.command;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record PatchPessoaCommand(

        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String nome,

        LocalDate dataNascimento,

        LocalDate dataAdmissao
) {}
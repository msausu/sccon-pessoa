package com.sccon.geo.pessoa.domain.port.in;
import com.sccon.geo.pessoa.application.command.AtualizarPessoaCommand;
import com.sccon.geo.pessoa.application.command.PatchPessoaCommand;
import com.sccon.geo.pessoa.domain.model.Pessoa;


public interface AtualizarPessoaUseCase {
    Pessoa atualizarDados(Long id, AtualizarPessoaCommand command);

    Pessoa patch(Long id, PatchPessoaCommand command);
}
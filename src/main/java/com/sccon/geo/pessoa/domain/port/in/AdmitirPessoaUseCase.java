package com.sccon.geo.pessoa.domain.port.in;

import com.sccon.geo.pessoa.application.command.AdmitirPessoaCommand;
import com.sccon.geo.pessoa.domain.model.Pessoa;

public interface AdmitirPessoaUseCase {
    Pessoa admitir(AdmitirPessoaCommand command);
}
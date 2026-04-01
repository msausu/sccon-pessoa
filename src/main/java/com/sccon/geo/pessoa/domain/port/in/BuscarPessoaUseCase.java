package com.sccon.geo.pessoa.domain.port.in;

import com.sccon.geo.pessoa.domain.model.Pessoa;

import java.util.List;

public interface BuscarPessoaUseCase {
    List<Pessoa> listarOrdenadoPorNome();
    Pessoa buscarPorId(Long id);
    boolean existsAny();
}
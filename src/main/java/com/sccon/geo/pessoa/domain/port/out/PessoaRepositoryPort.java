package com.sccon.geo.pessoa.domain.port.out;

import com.sccon.geo.pessoa.domain.model.Pessoa;

import java.util.*;

public interface PessoaRepositoryPort {
    Optional<Pessoa> findById(Long id);
    List<Pessoa> findAll();
    Pessoa save(Pessoa pessoa, boolean exists);
    void deleteById(Long id);
    boolean existsAny();
    boolean existsById(Long id);
    boolean existsByNome(String nome);
    Long nextIdentity();
}
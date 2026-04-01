package com.sccon.geo.pessoa.application.service;

import com.sccon.geo.pessoa.application.command.AdmitirPessoaCommand;
import com.sccon.geo.pessoa.application.command.AtualizarPessoaCommand;
import com.sccon.geo.pessoa.application.command.PatchPessoaCommand;
import com.sccon.geo.pessoa.domain.model.Pessoa;
import com.sccon.geo.pessoa.domain.port.in.AdmitirPessoaUseCase;
import com.sccon.geo.pessoa.domain.port.in.AtualizarPessoaUseCase;
import com.sccon.geo.pessoa.domain.port.in.BuscarPessoaUseCase;
import com.sccon.geo.pessoa.domain.port.in.RemoverPessoaUseCase;
import com.sccon.geo.pessoa.domain.port.out.PessoaRepositoryPort;
import com.sccon.geo.pessoa.exception.ConflictException;
import com.sccon.geo.pessoa.exception.PessoaNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class PessoaApplicationService implements
        AdmitirPessoaUseCase,
        AtualizarPessoaUseCase,
        BuscarPessoaUseCase,
        RemoverPessoaUseCase {

    private final PessoaRepositoryPort repository;
    private final ConflictException PESSOA_EXISTE = new ConflictException("Pessoa já existe");

    public PessoaApplicationService(PessoaRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Pessoa admitir(AdmitirPessoaCommand cmd) {

        Long id = cmd.id();

        if (id == null) {
            if (repository.existsByNome(cmd.nome())) {
                throw PESSOA_EXISTE;
            }
            id = repository.nextIdentity();
        } else {
            if (repository.existsById(id)) {
                throw PESSOA_EXISTE;
            }
        }

        Pessoa pessoa = new Pessoa(
                id,
                cmd.nome(),
                cmd.dataNascimento(),
                cmd.dataAdmissao()
        );

        return repository.save(pessoa, false);
    }

    @Override
    public Pessoa atualizarDados(Long id, AtualizarPessoaCommand cmd) {

        if (repository.existsByNome(cmd.nome())) {
            throw PESSOA_EXISTE;
        }

        Pessoa pessoa = repository.findById(id)
                .orElseThrow(() -> new PessoaNotFoundException("Pessoa não encontrada"));

        pessoa.atualizarDados(cmd.nome(), cmd.dataNascimento());

        return repository.save(pessoa, true);
    }

    @Override
    public Pessoa patch(Long id, PatchPessoaCommand cmd) {

        if (repository.existsByNome(cmd.nome())) {
            throw PESSOA_EXISTE;
        }

        Pessoa pessoa = repository.findById(id)
                .orElseThrow(() -> new PessoaNotFoundException("Pessoa não encontrada"));

        pessoa.aplicarPatch(
                cmd.nome(),
                cmd.dataNascimento(),
                cmd.dataAdmissao()
        );

        return repository.save(pessoa, true);
    }

    @Override
    public List<Pessoa> listarOrdenadoPorNome() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Pessoa::getNome))
                .toList();
    }

    @Override
    public Pessoa buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new PessoaNotFoundException("Pessoa não encontrada"));
    }

    @Override
    public boolean existsAny() {
        return repository.existsAny();
    }

    @Override
    public void remover(Long id) {

        boolean exists = repository.findById(id).isPresent();

        if (!exists) {
            throw new PessoaNotFoundException("Pessoa não encontrada");
        }

        repository.deleteById(id);
    }
}
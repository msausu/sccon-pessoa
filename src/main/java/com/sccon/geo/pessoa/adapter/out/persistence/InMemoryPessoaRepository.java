package com.sccon.geo.pessoa.adapter.out.persistence;

import com.sccon.geo.pessoa.domain.model.Pessoa;
import com.sccon.geo.pessoa.domain.port.out.PessoaRepositoryPort;
import com.sccon.geo.pessoa.exception.ConflictException;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryPessoaRepository implements PessoaRepositoryPort {

    private final Map<Long, Pessoa> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    @Override
    public Optional<Pessoa> findById(Long id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Pessoa> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Pessoa save(Pessoa pessoa, boolean exists) {
        Long id = pessoa.getId();

        if (id != null && id <= 0) {
            throw new IllegalArgumentException("ID deve ser maior do que zero");
        }

        if (!exists && (id != null && storage.containsKey(id))) {
            throw new ConflictException("ID já existe");
        }

        if (id == null) {
            id = idGenerator.incrementAndGet();
            pessoa.setId(id);
        } else {
            final Long finalId = id;
            idGenerator.updateAndGet(current -> Math.max(current, finalId));
        }

        storage.put(id, pessoa);

        return pessoa;
    }

    @Override
    public void deleteById(Long id) {
        if (id != null) {
            storage.remove(id);
        }
    }

    @Override
    public boolean existsAny() {
        return !storage.isEmpty();
    }

    @Override
    public boolean existsById(Long id) {
        if (id == null) return false;
        return storage.containsKey(id);
    }

    @Override
    public boolean existsByNome(String nome) {
        return  storage.values().stream().anyMatch(pessoa -> pessoa.getNome().compareTo(nome) == 0);
    }

    @Override
    public Long nextIdentity() {
        return idGenerator.incrementAndGet();
    }
}
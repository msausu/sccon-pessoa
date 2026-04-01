package com.sccon.geo.pessoa.adapter.out.persistence;

import com.sccon.geo.pessoa.domain.model.Pessoa;
import com.sccon.geo.pessoa.exception.ConflictException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InMemoryPessoaRepositoryTest {

    private InMemoryPessoaRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryPessoaRepository();
    }

    @Test
    @DisplayName("Should save a new pessoa and generate ID")
    void shouldSaveAndGenerateId() {
        Pessoa pessoa = createPessoa(null);
        Pessoa saved = repository.save(pessoa, false);

        assertThat(saved.getId()).isNotNull();
        assertThat(repository.existsById(saved.getId())).isTrue();
    }

    @Test
    @DisplayName("Should save a pessoa with manual ID")
    void shouldSaveWithManualId() {
        Pessoa pessoa = createPessoa(100L);
        Pessoa saved = repository.save(pessoa, false);

        assertThat(saved.getId()).isEqualTo(100L);
        assertThat(repository.existsById(100L)).isTrue();
    }

    @Test
    @DisplayName("Should throw ConflictException when saving existing ID")
    void shouldThrowConflictException() {
        Pessoa p1 = createPessoa(1L);
        repository.save(p1, false);

        Pessoa p2 = createPessoa(1L);
        assertThatThrownBy(() -> repository.save(p2, false))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("Should find a pessoa by ID")
    void shouldFindById() {
        Pessoa p1 = repository.save(createPessoa(null), false);
        Optional<Pessoa> found = repository.findById(p1.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(p1.getId());
    }

    @Test
    @DisplayName("Should return empty Optional when ID not found")
    void shouldReturnEmptyOptional() {
        assertThat(repository.findById(999L)).isEmpty();
    }

    @Test
    @DisplayName("Should list all saved pessoas")
    void shouldFindAll() {
        repository.save(createPessoa(null), false);
        repository.save(createPessoa(null), false);

        List<Pessoa> all = repository.findAll();
        assertThat(all).hasSize(2);
    }

    @Test
    @DisplayName("Should delete a pessoa by ID")
    void shouldDeleteById() {
        Pessoa p1 = repository.save(createPessoa(null), false);
        repository.deleteById(p1.getId());

        assertThat(repository.existsById(p1.getId())).isFalse();
    }

    @Test
    @DisplayName("Should check if any pessoa exists")
    void shouldCheckExistsAny() {
        assertThat(repository.existsAny()).isFalse();
        repository.save(createPessoa(null), false);
        assertThat(repository.existsAny()).isTrue();
    }

    @Test
    @DisplayName("Should generate next identity correctly")
    void shouldGenerateNextIdentity() {
        Long first = repository.nextIdentity();
        Long second = repository.nextIdentity();

        assertThat(second).isEqualTo(first + 1);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for non-positive ID")
    void shouldThrowForNonPositiveId() {
        Pessoa p1 = createPessoa(0L);
        assertThatThrownBy(() -> repository.save(p1, false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Pessoa createPessoa(Long id) {
        return new Pessoa(
                id,
                "Test User",
                LocalDate.of(1990, 1, 1),
                LocalDate.of(2023, 1, 1)
        );
    }
}
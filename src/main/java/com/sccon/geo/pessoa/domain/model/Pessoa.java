package com.sccon.geo.pessoa.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sccon.geo.pessoa.exception.DomainException;

import java.time.LocalDate;
import java.util.Objects;

public class Pessoa {

    private Long id;

    private String nome;

    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
    private LocalDate dataNascimento;

    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
    private LocalDate dataAdmissao;

    public Pessoa(Long id, String nome, LocalDate dataNascimento, LocalDate dataAdmissao) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.dataAdmissao = dataAdmissao;
    }

    public Pessoa() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public LocalDate getDataAdmissao() { return dataAdmissao; }
    public void setDataAdmissao(LocalDate dataAdmissao) { this.dataAdmissao = dataAdmissao; }

    public void atualizarDados(String nome, LocalDate nascimento) {
        validarDatas(nascimento, this.dataAdmissao);

        this.nome = nome;
        this.dataNascimento = nascimento;
    }

    private void validarDatas(LocalDate nascimento, LocalDate admissao) {
        if (nascimento == null || admissao == null) return;

        if (admissao.isBefore(nascimento)) {
            throw new DomainException(
                    "Data de admissão não pode ser anterior à data de nascimento"
            );
        }
    }

    public void aplicarPatch(
            String nome,
            LocalDate nascimento,
            LocalDate admissao
    ) {
        String novoNome = nome != null ? nome : this.nome;
        LocalDate novoNascimento = nascimento != null ? nascimento : this.dataNascimento;
        LocalDate novaAdmissao = admissao != null ? admissao : this.dataAdmissao;

        validarDatas(novoNascimento, novaAdmissao);

        this.nome = novoNome;
        this.dataNascimento = novoNascimento;
        this.dataAdmissao = novaAdmissao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pessoa pessoa = (Pessoa) o;
        return Objects.equals(nome, pessoa.nome) &&
                Objects.equals(dataNascimento, pessoa.dataNascimento) &&
                Objects.equals(dataAdmissao, pessoa.dataAdmissao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, dataNascimento, dataAdmissao);
    }
}
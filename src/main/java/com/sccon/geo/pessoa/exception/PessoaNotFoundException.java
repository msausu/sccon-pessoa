package com.sccon.geo.pessoa.exception;

public class PessoaNotFoundException extends RuntimeException {
    public PessoaNotFoundException(String message) {
        super(message);
    }
}
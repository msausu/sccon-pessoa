package com.sccon.geo.pessoa.domain.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

public class SalaryCalculator {

    private final BigDecimal base;
    private final BigDecimal taxa;
    private final BigDecimal bonus;

    public SalaryCalculator(
            BigDecimal base,
            BigDecimal taxa,
            BigDecimal bonus) {

        this.base = base;
        this.taxa = taxa;
        this.bonus = bonus;
    }

    public BigDecimal calcular(LocalDate dataAdmissao, LocalDate hoje) {

        int anos = Period.between(dataAdmissao, hoje).getYears();

        BigDecimal salario = base;

        for (int i = 0; i < anos; i++) {
            salario = salario.multiply(taxa).add(bonus);
        }

        return salario;
    }

}
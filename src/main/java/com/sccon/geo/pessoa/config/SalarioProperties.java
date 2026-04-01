package com.sccon.geo.pessoa.config;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;
import java.time.LocalDate;

@ConfigurationProperties(prefix = "salario")
public class SalarioProperties {

    private BigDecimal minimoReferencia = new BigDecimal("1302.00");
    private BigDecimal taxa = new BigDecimal("1.18");
    private BigDecimal bonus = new BigDecimal("500.00");
    private BigDecimal base = new BigDecimal("1558.00");

    public BigDecimal getMinimoReferencia() {
        return minimoReferencia;
    }

    public void setMinimoReferencia(BigDecimal minimoReferencia) {
        this.minimoReferencia = minimoReferencia;
    }

    public BigDecimal getTaxa() {
        return taxa;
    }

    public void setTaxa(BigDecimal taxa) {
        this.taxa = taxa;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public BigDecimal getBase() {
        return base;
    }

    public void setBase(BigDecimal base) {
        this.base = base;
    }

    @PostConstruct
    public void validate() {
        if (base == null || taxa.compareTo(BigDecimal.ZERO) <= 0) {
            base = new BigDecimal("1558.00");
        }
        if (minimoReferencia == null || minimoReferencia.compareTo(BigDecimal.ZERO) <= 0) {
            minimoReferencia = new BigDecimal("1302.00");
        }
        if (taxa == null || taxa.compareTo(BigDecimal.ZERO) <= 0) {
            taxa = new BigDecimal("1.18");
        }
        if (bonus == null || bonus.compareTo(BigDecimal.ZERO) < 0) {
            bonus = new BigDecimal("500.00");
        }
    }
}
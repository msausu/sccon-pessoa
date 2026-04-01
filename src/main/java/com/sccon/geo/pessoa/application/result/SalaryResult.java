package com.sccon.geo.pessoa.application.result;

import java.math.BigDecimal;

public record SalaryResult(
        Long pessoaId,
        BigDecimal valor,
        SalaryOutput tipo
) {}
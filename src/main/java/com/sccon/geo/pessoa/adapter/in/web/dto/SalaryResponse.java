package com.sccon.geo.pessoa.adapter.in.web.dto;

import com.sccon.geo.pessoa.application.result.SalaryResult;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public record SalaryResponse(
        Long pessoaId,
        String tipo,
        BigDecimal valor
) {
    public static SalaryResponse from(SalaryResult r) {
        return new SalaryResponse(
                r.pessoaId(),
                r.tipo().name(),
                r.valor()
        );
    }

    public static String getValorFormatted(SalaryResult r) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pt", "BR"));
        DecimalFormat df = new DecimalFormat("#,###,##0.00", symbols);  // or "#,##0.00"

        return df.format(r.valor());
    }
}
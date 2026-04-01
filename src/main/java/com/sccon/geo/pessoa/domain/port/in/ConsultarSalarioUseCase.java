package com.sccon.geo.pessoa.domain.port.in;

import com.sccon.geo.pessoa.application.result.SalaryOutput;
import com.sccon.geo.pessoa.application.result.SalaryResult;

public interface ConsultarSalarioUseCase {
    SalaryResult consultar(Long id, SalaryOutput salaryOutput);
}
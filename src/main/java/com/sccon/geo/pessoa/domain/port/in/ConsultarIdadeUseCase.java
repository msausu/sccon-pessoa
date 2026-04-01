package com.sccon.geo.pessoa.domain.port.in;

import com.sccon.geo.pessoa.application.result.AgeOutput;
import com.sccon.geo.pessoa.application.result.AgeResult;

public interface ConsultarIdadeUseCase {
    AgeResult consultar(Long id, AgeOutput output);
}
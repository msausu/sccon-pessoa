package com.sccon.geo.pessoa.domain.service;

import com.sccon.geo.pessoa.application.result.AgeOutput;

import java.time.LocalDate;
import java.time.Period;

import static java.time.temporal.ChronoUnit.*;

public class AgeCalculator {

    public long calcular(LocalDate nascimento, LocalDate hoje, AgeOutput output) {

        return switch (output) {
            case DAYS -> DAYS.between(nascimento, hoje);
            case MONTHS -> MONTHS.between(nascimento, hoje);
            case YEARS -> Period.between(nascimento, hoje).getYears();
        };
    }
}
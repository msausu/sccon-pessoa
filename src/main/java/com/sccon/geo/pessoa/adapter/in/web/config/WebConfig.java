package com.sccon.geo.pessoa.adapter.in.web.config;

import com.sccon.geo.pessoa.application.result.AgeOutput;
import com.sccon.geo.pessoa.application.result.SalaryOutput;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class WebConfig {

    @Bean
    public Converter<String, SalaryOutput> salaryOutputConverter() {
        return source -> {
            try {
                return SalaryOutput.valueOf(source.toUpperCase());
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid SalaryOutput: " + source);
            }
        };
    }

    @Bean
    public Converter<String, AgeOutput> ageOutputConverter() {
        return source -> {
            try {
                return AgeOutput.valueOf(source.toUpperCase());
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid AgeOutput: " + source);
            }
        };
    }
}
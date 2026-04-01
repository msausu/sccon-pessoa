package com.sccon.geo.pessoa.infrastructure.jackson;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.module.SimpleModule;

import java.math.BigDecimal;

@Configuration
public class JacksonConfig {

    @Bean
    public JsonMapperBuilderCustomizer jsonMapperCustomizer() {
        return builder -> {
            SimpleModule module = new SimpleModule();
            module.addSerializer(BigDecimal.class, new BigDecimalPtBrSerializer());

            builder.addModule(module);
            builder.build();
        };
    }
}
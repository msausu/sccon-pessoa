package com.sccon.geo.pessoa.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SalarioProperties.class)
public class AppConfig {
}
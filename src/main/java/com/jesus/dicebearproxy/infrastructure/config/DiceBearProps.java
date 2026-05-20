package com.jesus.dicebearproxy.infrastructure.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "dicebear")
public record DiceBearProps(
        @NotBlank String baseUrl,
        @NotBlank String version,
        @Min(100) int connectTimeoutMillis,
        @Min(100) int responseTimeoutMillis) {}

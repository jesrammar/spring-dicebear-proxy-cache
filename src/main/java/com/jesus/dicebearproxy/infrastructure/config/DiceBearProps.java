package com.jesus.dicebearproxy.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "dicebear")
public record DiceBearProps(String baseUrl, String version) {}

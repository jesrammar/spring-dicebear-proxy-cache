package com.jesus.dicebearproxy.infrastructure.config;

import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheKeyConfig {

  @Bean
  public KeyGenerator avatarKeyGen() {
    return (t, m, p) -> {
      String seed = (String) p[0];
      String style = (String) p[1];
      @SuppressWarnings("unchecked")
      Map<String,String> all = (Map<String,String>) p[2];
      String canon = all.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .map(e -> e.getKey() + "=" + e.getValue())
        .collect(Collectors.joining("&"));
      return seed + ":" + style + ":" + canon;
    };
  }
}

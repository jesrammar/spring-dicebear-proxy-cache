package com.jesus.dicebearproxy.infrastructure.config;

import io.netty.channel.ChannelOption;
import java.time.Duration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
@EnableConfigurationProperties(DiceBearProps.class)
public class WebClientConfig {

  @Bean("diceBearWebClient")
  public WebClient diceBearWebClient(DiceBearProps p) {
    HttpClient http = HttpClient.create()
      .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, p.connectTimeoutMillis())
      .responseTimeout(Duration.ofMillis(p.responseTimeoutMillis()));

    return WebClient.builder()
      .baseUrl(p.baseUrl() + "/" + p.version())
      .clientConnector(new ReactorClientHttpConnector(http))
      .defaultHeader(HttpHeaders.ACCEPT, "image/svg+xml")
      .build();
  }
}

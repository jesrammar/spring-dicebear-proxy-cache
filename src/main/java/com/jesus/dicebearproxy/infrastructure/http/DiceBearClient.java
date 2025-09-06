package com.jesus.dicebearproxy.infrastructure.http;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

@Component
public class DiceBearClient {

  private final WebClient web;

  public DiceBearClient(@Qualifier("diceBearWebClient") WebClient web) {
    this.web = web;
  }

  @Retry(name = "dicebear")
  @TimeLimiter(name = "dicebear")
  @CircuitBreaker(name = "dicebear", fallbackMethod = "fallback")
  public byte[] fetch(String style, MultiValueMap<String,String> params) {
    return web.get()
      .uri(b -> b.path("/" + style + "/svg").queryParams(params).build())
      .retrieve()
      .onStatus(HttpStatusCode::isError, r -> r.createException())
      .bodyToMono(byte[].class)
      .block();
  }

  private byte[] fallback(String style, MultiValueMap<String,String> params, Throwable ex) {
    return "<svg xmlns='http://www.w3.org/2000/svg' width='1' height='1'/>"
      .getBytes(StandardCharsets.UTF_8);
  }
}

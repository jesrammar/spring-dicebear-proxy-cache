package com.jesus.dicebearproxy.infrastructure.client;

import com.jesus.dicebearproxy.infrastructure.exception.UpstreamServiceException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class DiceBearClient {

    private final WebClient diceBearWebClient;

    public DiceBearClient(@Qualifier("diceBearWebClient") WebClient diceBearWebClient) {
        this.diceBearWebClient = diceBearWebClient;
    }

    public byte[] fetchAvatarSvg(String seed, String style, Map<String, String> allParams) {
        return diceBearWebClient
                .get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder.path("/{style}/svg");

                    allParams.forEach((key, value) -> {
                        if (!"seed".equals(key) && !"style".equals(key)) {
                            builder.queryParam(key, value);
                        }
                    });

                    builder.queryParam("seed", seed);
                    return builder.build(style);
                })
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                        .defaultIfEmpty("")
                        .flatMap(body -> Mono.error(new UpstreamServiceException(
                                "DiceBear responded with status %s. %s"
                                        .formatted(response.statusCode().value(), body)))))
                .bodyToMono(byte[].class)
                .block();
    }
}

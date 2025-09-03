package com.jesus.dicebearproxy;


import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@Tag(name = "DiceBear Proxy")
public class AvatarController {

    private static final String IMAGE_SVG_XML_VALUE = "image/svg+xml";

    private final WebClient web = WebClient.builder()
            .baseUrl("https://api.dicebear.com/7.x")
            .build();

    @Operation(summary = "Proxy con cache y reintentos")
    @GetMapping(value = "/avatar/{seed}", produces = IMAGE_SVG_XML_VALUE)
    @Cacheable(cacheNames = "avatars",
               key = "#seed + ':' + (#style==null?'adventurer':#style) + ':' + #all.toString()")
    @Retry(name = "dicebear")
    public ResponseEntity<byte[]> get(
    @PathVariable("seed") String seed,
    @RequestParam(name = "style", required = false, defaultValue = "adventurer") String style,
    @RequestParam Map<String, String> all) {

        StringBuilder qs = new StringBuilder();
        for (Map.Entry<String, String> e : all.entrySet()) {
            if (qs.length() > 0) qs.append('&');
            qs.append(URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8)).append('=')
              .append(URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8));
        }
        if (!all.containsKey("seed")) {
            if (qs.length() > 0) qs.append('&');
            qs.append("seed=").append(URLEncoder.encode(seed, StandardCharsets.UTF_8));
        }

        String uri = "/" + style + "/svg" + (qs.length() > 0 ? "?" + qs : "");
        byte[] body = web.get().uri(uri).retrieve().bodyToMono(byte[].class).block();

        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.valueOf("image/svg+xml; charset=utf-8"));
        h.setCacheControl("public, max-age=86400");

        return new ResponseEntity<>(body, h, HttpStatus.OK);
    }
}

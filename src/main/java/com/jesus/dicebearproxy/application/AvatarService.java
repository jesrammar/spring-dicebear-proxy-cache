package com.jesus.dicebearproxy.application;

import com.jesus.dicebearproxy.infrastructure.client.DiceBearClient;
import com.jesus.dicebearproxy.infrastructure.exception.UpstreamServiceException;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class AvatarService {

    private static final Logger log = LoggerFactory.getLogger(AvatarService.class);
    private static final String DEFAULT_STYLE = "adventurer";
    private final DiceBearClient diceBearClient;

    public AvatarService(DiceBearClient diceBearClient) {
        this.diceBearClient = diceBearClient;
    }

    @Cacheable(cacheNames = "avatars", keyGenerator = "avatarKeyGen")
    @Retry(name = "dicebear", fallbackMethod = "avatarFallback")
    public byte[] getAvatarSvg(String seed, String style, Map<String, String> allParams) {
        String effectiveStyle = normalizeStyle(style);
        log.info("Fetching avatar from DiceBear. seed={}, style={}, params={}", seed, effectiveStyle, allParams);
        return diceBearClient.fetchAvatarSvg(seed, effectiveStyle, allParams);
    }

    @SuppressWarnings("unused")
    public byte[] avatarFallback(String seed, String style, Map<String, String> allParams, Exception ex) {
        String effectiveStyle = normalizeStyle(style);
        throw new UpstreamServiceException(
                "DiceBear request failed after retry exhaustion for seed=%s and style=%s"
                        .formatted(seed, effectiveStyle),
                ex);
    }

    private String normalizeStyle(String style) {
        return (style == null || style.isBlank()) ? DEFAULT_STYLE : style;
    }
}

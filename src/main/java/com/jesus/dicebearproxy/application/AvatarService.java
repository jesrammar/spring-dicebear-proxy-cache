package com.jesus.dicebearproxy.application;

import com.jesus.dicebearproxy.infrastructure.http.DiceBearClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.Duration;
import java.util.Map;

@Service
public class AvatarService {

  private final DiceBearClient client;

  public AvatarService(DiceBearClient client) {
    this.client = client;
  }

  @Cacheable(cacheNames = "avatars", keyGenerator = "avatarKeyGen")
  public ResponseEntity<byte[]> get(String seed, String style, Map<String,String> all) {
    MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
    all.forEach(params::add);
    params.addIfAbsent("seed", seed);

    byte[] body = client.fetch(style, params);

    String etag = "W/\"" + DigestUtils.md5DigestAsHex(body) + "\"";
    return ResponseEntity.ok()
      .contentType(MediaType.valueOf("image/svg+xml; charset=utf-8"))
      .cacheControl(CacheControl.maxAge(Duration.ofDays(1)).cachePublic())
      .eTag(etag)
      .body(body);
  }
}

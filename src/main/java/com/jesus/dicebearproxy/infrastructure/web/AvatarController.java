package com.jesus.dicebearproxy.infrastructure.web;

import com.jesus.dicebearproxy.application.AvatarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Validated
@Tag(name = "DiceBear Proxy")
public class AvatarController {

  private final AvatarService service;

  public AvatarController(AvatarService service) { this.service = service; }

  @Operation(summary = "Proxy con caché y resiliencia")
  @GetMapping(value = "/avatar/{seed}", produces = "image/svg+xml")
  public ResponseEntity<byte[]> get(
      @PathVariable @Pattern(regexp = "^[A-Za-z0-9-_]{1,64}$") String seed,
      @RequestParam(defaultValue = "adventurer")
      @Pattern(regexp = "^(adventurer|bottts|thumbs|rings|identicon|initials)$") String style,
      @RequestParam Map<String,String> all) {
    return service.get(seed, style, all);
  }
}

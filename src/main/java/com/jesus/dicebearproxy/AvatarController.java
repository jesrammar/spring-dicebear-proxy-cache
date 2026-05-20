package com.jesus.dicebearproxy;

import com.jesus.dicebearproxy.application.AvatarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Map;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/avatar")
@Tag(name = "DiceBear Proxy")
public class AvatarController {

    private static final MediaType SVG_MEDIA_TYPE =
            MediaType.parseMediaType("image/svg+xml; charset=utf-8");

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @Operation(
            summary = "Generate avatar SVG through DiceBear proxy",
            description = "Returns an SVG avatar generated from the provided seed. "
                    + "If style is not provided, the default style is adventurer.")
    @GetMapping(value = "/{seed}", produces = "image/svg+xml")
    public ResponseEntity<byte[]> getAvatar(
            @Parameter(description = "Unique avatar seed", example = "testuser")
            @PathVariable
            @Size(min = 1, max = 100, message = "seed must be between 1 and 100 characters")
            String seed,
            @Parameter(description = "DiceBear style", example = "bottts")
            @RequestParam(name = "style", required = false)
            @Pattern(
                    regexp = "^[a-zA-Z0-9-]{1,40}$",
                    message = "style must contain only letters, numbers or hyphens")
            String style,
            @Parameter(hidden = true) @RequestParam Map<String, String> allParams) {

        byte[] body = avatarService.getAvatarSvg(seed, style, allParams);

        return ResponseEntity.ok()
                .contentType(SVG_MEDIA_TYPE)
                .cacheControl(CacheControl.maxAge(java.time.Duration.ofHours(24)).cachePublic())
                .header(HttpHeaders.VARY, "Accept")
                .body(body);
    }
}
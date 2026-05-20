package com.jesus.dicebearproxy;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.http.*;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AvatarControllerTest {

    private static final String LONG_SEED = "x".repeat(101);

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig().dynamicPort())
            .build();

    @Autowired
    private TestRestTemplate restTemplate;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("dicebear.base-url", wireMock::baseUrl);
        registry.add("dicebear.version", () -> "7.x");
        registry.add("dicebear.connect-timeout-millis", () -> 2_000);
        registry.add("dicebear.response-timeout-millis", () -> 3_000);
    }

    @Test
    void testAvatarEndpoint() {
        wireMock.stubFor(get(urlEqualTo("/7.x/bottts/svg?seed=testseed"))
                .willReturn(ok()
                        .withHeader(HttpHeaders.CONTENT_TYPE, "image/svg+xml; charset=utf-8")
                        .withBody("<svg>ok</svg>")));

        ResponseEntity<byte[]> response =
        restTemplate.getForEntity("/avatar/testseed?style=bottts", byte[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().toString()).contains("image/svg+xml");
        assertThat(response.getBody()).isNotNull();
        assertThat(new String(response.getBody())).contains("<svg>ok</svg>");
    }

    @Test
    void testAvatarEndpoint_invalidStyle() {
        wireMock.stubFor(get(urlEqualTo("/7.x/invalidstyle/svg?seed=testseed"))
                .willReturn(aResponse().withStatus(404).withBody("style not found")));

        ResponseEntity<String> response = restTemplate.getForEntity("/avatar/testseed?style=invalidstyle", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY);
        assertThat(response.getHeaders().getContentType().toString()).contains("application/json");
        assertThat(response.getBody()).contains("DiceBear responded with status 404");
    }


    @Test
    void testAvatarEndpoint_defaultStyle() {
        wireMock.stubFor(get(urlEqualTo("/7.x/adventurer/svg?seed=testseed"))
                .willReturn(ok()
                        .withHeader(HttpHeaders.CONTENT_TYPE, "image/svg+xml; charset=utf-8")
                        .withBody("<svg>default</svg>")));

        ResponseEntity<byte[]> response =
            restTemplate.getForEntity("/avatar/testseed", byte[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().toString()).contains("image/svg+xml");
        assertThat(response.getBody()).isNotNull();
        assertThat(new String(response.getBody())).contains("<svg>default</svg>");
    }

    @Test
    void testAvatarEndpoint_usesCacheForSameRequest() {
        wireMock.stubFor(get(urlEqualTo("/7.x/bottts/svg?size=64&seed=testseed"))
                .willReturn(ok()
                        .withHeader(HttpHeaders.CONTENT_TYPE, "image/svg+xml; charset=utf-8")
                        .withBody("<svg>cached</svg>")));

        ResponseEntity<byte[]> firstResponse =
                restTemplate.getForEntity("/avatar/testseed?style=bottts&size=64", byte[].class);
        ResponseEntity<byte[]> secondResponse =
                restTemplate.getForEntity("/avatar/testseed?style=bottts&size=64", byte[].class);

        assertThat(firstResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(secondResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        wireMock.verify(1, getRequestedFor(urlEqualTo("/7.x/bottts/svg?size=64&seed=testseed")));
    }

    @Test
    void testAvatarEndpoint_rejectsInvalidStyleFormat() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/avatar/testseed?style=bad/style", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getHeaders().getContentType().toString()).contains("application/json");
        assertThat(response.getBody()).contains("style must contain only letters, numbers or hyphens");
    }

    @Test
    void testAvatarEndpoint_rejectsTooLongSeed() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/avatar/" + LONG_SEED + "?style=bottts", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getHeaders().getContentType().toString()).contains("application/json");
        assertThat(response.getBody()).contains("seed must be between 1 and 100 characters");
    }

}

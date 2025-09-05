package com.jesus.dicebearproxy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AvatarControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testAvatarEndpoint() {
        ResponseEntity<byte[]> response =
        restTemplate.getForEntity("/avatar/testseed?style=bottts", byte[].class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().toString()).contains("image/svg+xml");
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testAvatarEndpoint_invalidStyle() {
        ResponseEntity<String> response =
            restTemplate.getForEntity("/avatar/testseed?style=invalidstyle", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY);
        assertThat(response.getBody()).contains("Error fetching avatar");
    }


    @Test
    void testAvatarEndpoint_defaultStyle() {
        ResponseEntity<byte[]> response =
            restTemplate.getForEntity("/avatar/testseed", byte[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().toString()).contains("image/svg+xml");
        assertThat(response.getBody()).isNotNull();
    }


    

}

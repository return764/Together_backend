package com.cn.yutao.together_backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TogetherBackendApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void should_hello_world() {
        final var responseEntity = restTemplate.getForEntity("/hello", String.class);

        assertThat(responseEntity.getBody()).isEqualTo("World");
    }

}

package ch.canaweb.api;

import ch.canaweb.api.core.Field.Field;
import ch.canaweb.api.persistence.FieldRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient()
public class FieldControllerTest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private FieldRepository repository;

    @Test
    void getAllFields() {
        this.webClient.get()
                .uri("/api/field/all")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Field.class);
    }

    @Test
    void hello() {
        this.webClient.get()
                .uri("/hello")
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    void user() {
        this.webClient.get()
                .uri("/api/user")
                .exchange()
                .expectStatus().is2xxSuccessful();
    }
}

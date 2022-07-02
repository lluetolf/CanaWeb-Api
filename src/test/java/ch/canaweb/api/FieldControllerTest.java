package ch.canaweb.api;

import ch.canaweb.api.core.Field.Field;
import ch.canaweb.api.persistence.FieldRepository;
import com.google.cloud.Timestamp;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;



@SpringBootTest
@AutoConfigureWebTestClient()
public class FieldControllerTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private FieldRepository repository;
    private Object List;

    @Order(1)
    @Test
    void getAllFields() {
        this.webClient.get()
                .uri("/api/field/all")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful();

    }

    @Order(2)
    @Test
    void createField() {
        Mono<Field> new_field = Mono.just(new Field(
                null,
                "test-1",
                "test-1",
                "test-1",
                9.0,
                10.0,
                Timestamp.now(),
                "test-1"
//                Timestamp.valueOf(LocalDateTime.now())
                )
        );

        Field f = this.webClient
                .post()
                .uri("/api/field")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(new_field, Field.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Field.class).getResponseBody().blockFirst();

        this.logger.info(f.toString());
    }

    @Order(3)
    @Test
    void getAllFields2() {
        this.webClient.get()
                .uri("/api/field/all")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful();

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

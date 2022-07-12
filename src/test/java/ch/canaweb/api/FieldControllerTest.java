package ch.canaweb.api;

import ch.canaweb.api.core.Field.Field;
import ch.canaweb.api.error.AbstracHttpException;
import ch.canaweb.api.persistence.FieldRepository;
import com.google.cloud.Timestamp;
import net.datafaker.Faker;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertTrue;


@SpringBootTest
@AutoConfigureWebTestClient()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FieldControllerTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Faker faker = new Faker();

    private static int fieldCnt = 0;
    private static Field createdField = null;

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private FieldRepository repository;


    private Field generateRandomField() {
        return new Field(
                null,
                this.faker.random().hex(),
                this.faker.residentEvil().equipment(),
                this.faker.residentEvil().character(),
                this.faker.random().nextDouble(0, 100),
                this.faker.random().nextDouble(0, 100),
                Timestamp.now(),
                this.faker.random().hex(),
                Timestamp.now()
        );
    }

    @Test
    void hello() {
        this.webClient.get()
                .uri("/hello")
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    @Order(1)
    void getAllFields() {
        List<Field> fields = this.webClient.get()
                .uri("/api/field/all")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Field.class)
                .returnResult().getResponseBody();

        FieldControllerTest.fieldCnt = fields.size();
        this.logger.info("Found " + fields.size());
    }

    @Test
    @Order(2)
    void createField() {
        Field f = generateRandomField();
        Mono<Field> new_field = Mono.just(f);
        FieldControllerTest.createdField = f;

        Field field = this.webClient
                .post()
                .uri("/api/field")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(new_field, Field.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Field.class)
                .returnResult().getResponseBody();


        this.logger.info(f.toString());
    }

    @Test
    @Order(3)
    void createDuplicateField() {
        Field f = FieldControllerTest.createdField;
        f.setId(null);

        this.webClient
                .post()
                .uri("/api/field")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(f), Field.class)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.type").isEqualTo("DuplicateHttpException")
                .jsonPath("$.message").isNotEmpty()
                .jsonPath("$.details").isEqualTo("no details");
    }

    @Test
    @Order(3)
    void getAllFields2() {
        List<Field> fields = this.webClient.get()
                .uri("/api/field/all")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Field.class)
                .returnResult().getResponseBody();

        assertTrue("Field got added successfully.", fields.size() == FieldControllerTest.fieldCnt + 1);
    }

    @Test
    @Order(3)
    void getByNameAndUpdate() {
        String url = "/api/field/name/" + FieldControllerTest.createdField.getName();
        Field field = this.webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Field.class)
                .returnResult().getResponseBody();

        field.setName("New_Name");
        field.setIngenioId("NEW_INGENIO_ID");

        Field updatedField = this.webClient.put()
                .uri("/api/field/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(field), Field.class)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Field.class)
                .returnResult().getResponseBody();

        assertTrue("Name updated", "New_Name".equals(updatedField.getName()));
        assertTrue("IngenioId updated", "NEW_INGENIO_ID".equals(updatedField.getIngenioId()));
    }

    @Test
    @Order(4)
    void deleteField() {
        Field f = FieldControllerTest.createdField;
        f.setId(null);

        String url = "/api/field/name/" + "New_Name";
        this.webClient.delete()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful();

    }


    @Test
    @Order(5)
    void getAllFields3() {
        List<Field> fields = this.webClient.get()
                .uri("/api/field/all")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Field.class)
                .returnResult().getResponseBody();

        assertTrue("Field got deleted successfully.", fields.size() == FieldControllerTest.fieldCnt);
    }




}

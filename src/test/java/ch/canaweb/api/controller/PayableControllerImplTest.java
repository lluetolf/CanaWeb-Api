package ch.canaweb.api.controller;

import ch.canaweb.api.core.Payable.Payable;
import ch.canaweb.api.persistence.PayableRepository;
import com.google.cloud.Timestamp;
import net.datafaker.Faker;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureWebTestClient()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PayableControllerImplTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Faker faker = new Faker();

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private PayableRepository repository;

    private List<Payable> createBaseSet() {
        ArrayList<Payable> payables = new ArrayList<>();

        for (int i = 0; i < faker.random().nextInt(3, 12); i++)
            payables.add(createPayable());

        return payables;
    }

    private Payable createPayable() {
        int secondsPerYear = 365*24*60*60;
        long today = 1657639148 - secondsPerYear;
        return new Payable(
                null,
                Timestamp.ofTimeSecondsAndNanos(today + faker.random().nextInt(0, secondsPerYear), 0),
                faker.random().nextDouble(1657639148, 10000),
                faker.random().nextInt(0, 100),
                faker.random().nextInt(100, 10000),
                List.of(this.faker.random().hex(), this.faker.random().hex()),
                faker.residentEvil().equipment(),
                faker.residentEvil().biologicalAgent(),
                faker.dessert().variety(),
                Timestamp.now()
        );
    }



    @BeforeAll
    void setUp() {
        this.logger.info("Setup");
        this.repository.deleteAll().block();
        this.repository.saveAll(createBaseSet()).blockLast();
    }

    @AfterAll
    void tearDown() {
        this.logger.info("TearDown");
    }

    @Test
    void getVersion() {
        String version = this.webClient.get()
                .uri("/api/payable/version")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        assertTrue("V1.0".equals(version), "Version is 1.0");
    }


    @Test
    void getAllPayablesBetween() {

        int offset = faker.random().nextInt(90, 365);
        String from = LocalDate.now().minusDays(offset).format(DateTimeFormatter.ISO_DATE);
        String until = LocalDate.now().minusDays(offset - 90).format(DateTimeFormatter.ISO_DATE);
        this.logger.info("Get: " + from + " until " +until);

        List<Payable> payables = this.webClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/payable/between")
                                .queryParam("from", from)
                                .queryParam("until", until)
                                .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Payable.class)
                .returnResult()
                .getResponseBody();
    }
}
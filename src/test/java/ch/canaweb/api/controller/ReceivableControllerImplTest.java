package ch.canaweb.api.controller;

import ch.canaweb.api.core.Receivable.Phase;
import ch.canaweb.api.core.Receivable.Receivable;
import ch.canaweb.api.persistence.ReceivableRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureWebTestClient()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReceivableControllerImplTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Faker faker = new Faker();

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private ReceivableRepository repository;

    private List<Receivable> createBaseSet() {
        ArrayList<Receivable> receivables = new ArrayList<>();

        receivables.add(generateReceivable());

        return receivables;
    }

    private Receivable generateReceivable() {
        int secondsPerYear = 365 * 24 * 60 * 60;
        long today = LocalDate.now().toEpochSecond(LocalTime.MIN, ZoneOffset.UTC) - secondsPerYear;
        String ingenioId = this.faker.random().hex();
        int year = faker.random().nextInt(18, 25);
        String harvest = year + "-" + (year + 1);
        return new Receivable(
                this.faker.random().hex(),
                new Phase(ingenioId, faker.random().nextDouble(50, 5000), faker.random().nextDouble(1, 5000), faker.random().nextDouble(100, 1000)),
                new Phase(ingenioId, faker.random().nextDouble(50, 5000), faker.random().nextDouble(1, 5000), faker.random().nextDouble(100, 1000)),
                new Phase(ingenioId, faker.random().nextDouble(50, 5000), faker.random().nextDouble(1, 5000), faker.random().nextDouble(100, 1000)),
                harvest
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
    void getAllReceivables() {
        List<Receivable> receivables = this.webClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/receivable/all")
                                .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Receivable.class)
                .returnResult()
                .getResponseBody();

        var repoList = this.repository.findAll().collectList().block();
        assert repoList != null;
        assertEquals(repoList.size(), receivables.size(), "Count of all receivables matche.");
    }
}
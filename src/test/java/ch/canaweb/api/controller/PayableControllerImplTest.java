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
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
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

    private final Faker faker = new Faker();

    private List<Payable> allPayables;
    private Payable createdPayable;


    @Autowired
    private WebTestClient webClient;

    @Autowired
    private PayableRepository repository;


    private List<Payable> createBaseSet() {
        ArrayList<Payable> payables = new ArrayList<>();

        for (int i = 0; i < faker.random().nextInt(36, 72); i++)
            payables.add(generatePayable());

        return payables;
    }

    private Payable generatePayable() {
        int secondsPerYear = 365*24*60*60;
        long today = LocalDate.now().toEpochSecond(LocalTime.MIN, ZoneOffset.UTC) - secondsPerYear;
        return new Payable(
                this.faker.random().hex(),
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
    void getAllPayables() {

        List<Payable> payables = this.webClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/payable/all")
                                .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Payable.class)
                .returnResult()
                .getResponseBody();

        var repoList = this.repository.findAll().collectList().block();
        assertTrue(repoList.size() == payables.size(), "Count of all payables matche.");
    }


    @Test
    void getAllPayablesBetween() {
        int offset = faker.random().nextInt(90, 365);

        LocalDate fromDate = LocalDate.now().minusDays(offset);
        LocalDate untilDate = LocalDate.now().minusDays(offset - 90);
        String from = fromDate.format(DateTimeFormatter.ISO_DATE);
        String until = untilDate.format(DateTimeFormatter.ISO_DATE);
        Timestamp fromTS = Timestamp.of(java.sql.Timestamp.valueOf(fromDate.atStartOfDay()));
        Timestamp untilTS = Timestamp.of(java.sql.Timestamp.valueOf(untilDate.atStartOfDay()));
        this.logger.info("Get: " + from + " until " + until);

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

        var repoList = this.repository.findByTransactionDateGreaterThanEqualAndTransactionDateLessThanEqual(fromTS, untilTS).collectList().block();
        assertTrue(repoList.size() == payables.size(), "Count of payables between Dates matche.");
    }

    @Test
    void getAllPayablesForMonth() {
        int year = 2022;
        int month = 4;

        this.logger.info("Year: " + year + ", month:" + month);
        List<Payable> payables = this.webClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/payable/month")
                                .queryParam("year", year)
                                .queryParam("month", month)
                                .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Payable.class)
                .returnResult()
                .getResponseBody();

        YearMonth m = YearMonth.of(year, month);
        Timestamp fromTS = Timestamp.ofTimeSecondsAndNanos(m.atDay(1).toEpochSecond(LocalTime.MIN, ZoneOffset.UTC), 0);
        Timestamp untilTS = Timestamp.ofTimeSecondsAndNanos(m.atEndOfMonth().toEpochSecond(LocalTime.MAX, ZoneOffset.UTC), 0);
        this.logger.info("Get: " + fromTS.toDate().toString() + " until " + untilTS.toDate().toString() );
        var repoList = this.repository.findByTransactionDateGreaterThanEqualAndTransactionDateLessThanEqual(fromTS, untilTS).collectList().block();
        assertTrue(repoList.size() == payables.size(), "Count of payables between Dates matche.");
    }

    @Test
    @Order(1)
    void createPayable() {
        Payable newPayable = generatePayable();
        this.createdPayable = newPayable;
        this.logger.info("Create Payable: " + newPayable.getPayableId());

        Payable returned = this.webClient.post()
                .uri("/api/payable")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(newPayable), Payable.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Payable.class)
                .returnResult().getResponseBody();
    }

    @Test
    @Order(2)
    void getPayablebyId() {
        String payableId = this.createdPayable.getPayableId();

        Payable returned = this.webClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/payable")
                                .queryParam("payableId", payableId)
                                .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Payable.class)
                .returnResult().getResponseBody();

        assertTrue(payableId.equals(returned.getPayableId()), "Unable to fetch Payable by id.");
    }

    @Test
    @Order(2)
    void updatePayable() {
        Payable updatedPayable = null;
        try {
            updatedPayable = this.createdPayable.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        String newComment = "Got Updated";
        updatedPayable.setComment(newComment);

        Payable p = this.webClient.put()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/payable")
                                .queryParam("payableId", this.createdPayable.getPayableId())
                                .build())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedPayable), Payable.class)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Payable.class)
                .returnResult().getResponseBody();

        assertTrue(newComment.equals(p.getComment()), "Commment updated.");
    }

    @Test
    @Order(3)
    void deletePayable() {
        String payableId = this.createdPayable.getPayableId();

        assertTrue(this.repository.findById(payableId).block() != null, "Payable to delete does not exist.");

        this.webClient.delete()
                .uri("/api/payable/" + payableId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful();

        assertTrue(this.repository.findById(payableId).block() == null, "Payable was not deleted.");
    }
}
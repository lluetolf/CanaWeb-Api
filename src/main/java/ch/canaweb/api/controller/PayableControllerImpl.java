package ch.canaweb.api.controller;

import ch.canaweb.api.core.Payable.Payable;
import ch.canaweb.api.core.Payable.PayableService;
import ch.canaweb.api.persistence.FieldRepository;
import ch.canaweb.api.persistence.PayableRepository;
import com.google.cloud.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;

@RestController
public class PayableControllerImpl implements PayableService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private PayableRepository repository;

    @Autowired
    public void setRepository(PayableRepository repository){
        this.repository=repository;
    }

    @Override
    public Mono<String> getVersion() {
        return Mono.just("V1.0");
    }

    @Override
    public Mono<Payable> getPayable(String payableId) {
        this.logger.info(String.format("%1$s(%2$s)", "getPayable", payableId));

        return this.repository.findById(payableId);
    }

    @Override
    public Flux<Payable> getAllPayables() {
        this.logger.info("getAllPayables()");
        return this.repository.findAll();
    }

    @Override
    public Mono<List<Payable>> getAllPayablesBetween(LocalDate from, LocalDate until) {
        this.logger.info(String.format("%1$s(%2$s, %2$s)", "getAllPayablesBetween", from.toString(), until.toString()));

        Timestamp fromTS = Timestamp.ofTimeSecondsAndNanos(from.toEpochSecond(LocalTime.MIN, ZoneOffset.UTC), 0);
        Timestamp untilTS = Timestamp.ofTimeSecondsAndNanos(until.toEpochSecond(LocalTime.MIN, ZoneOffset.UTC), 0);

        return repository.findByTransactionDateGreaterThanEqualAndTransactionDateLessThanEqual(fromTS, untilTS)
                .collectList()
                .doOnNext(x -> {
                    this.logger.info("Payables: " + x.size());
                });
    }

    @Override
    public Mono<Payable> createPayable(Payable body) {
        this.logger.info(String.format("%1$s(%2$s)", "createPayable", body.getPayableId()));

        return this.repository.save(body);
    }

    @Override
    public Mono<Payable> updatePayable(Payable body) {
        return null;
    }

    @Override
    public Mono<Void> deletePayable(int payableId) {
        return null;
    }
}

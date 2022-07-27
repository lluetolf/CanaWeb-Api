package ch.canaweb.api.controller;

import ch.canaweb.api.core.Payable.Payable;
import ch.canaweb.api.core.Payable.PayableService;
import ch.canaweb.api.error.BaseHttpException;
import ch.canaweb.api.error.DuplicateHttpException;
import ch.canaweb.api.error.EntityDoesNotExistHttpException;
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
                .doOnNext(x -> this.logger.info("Payables: " + x.size()));
    }

    @Override
    public Mono<Payable> createPayable(Payable payable) {
        this.logger.info(String.format("%1$s(%2$s)", "createPayable", payable.getPayableId()));

        try {
            return this.repository.findById(payable.getPayableId())
                    .hasElement()
                    .flatMap(x -> {
                        if (x) {
                            this.logger.info("Payable exists already.");
                            return Mono.error(new DuplicateHttpException("There is already a Field with name: " + payable.getPayableId(), "no details"));
                        } else {
                            this.logger.info("Create Payable.");
                            payable.setLastUpdated(Timestamp.now());
                            return this.repository.save(payable).log();
                        }
                    })
                    .onErrorMap( e -> new BaseHttpException("Failed to create new Payable", e.getMessage()) );
        } catch (Exception e) {
            this.logger.error("ERROR CAUGHT: "  + e.getMessage());
            e.printStackTrace();
        }

        return Mono.error(new BaseHttpException("Error", "failed"));
    }

    @Override
    public Mono<Payable> updatePayable(Payable payable, String payableId) {
        this.logger.info(String.format("%1$s(%2$s, %3$s)", "updatePayable", payable.getPayableId(), payableId));

        if(payableId != null) {
            if(!payableId.equals(payable.getPayableId()))
                return Mono.error(new BaseHttpException("RequestParam Id does not match payload.", payableId));
        }

        return this.repository.findById(payableId)
                .hasElement()
                .flatMap(x -> {
                    if(x) {
                        payable.setLastUpdated(Timestamp.now());
                        return this.repository.save(payable);
                    } else {
                        this.logger.info("Can't update payable for none existing id: " + payable.getPayableId());
                        return Mono.error(new BaseHttpException("Can't update payable for none existing id.", "ID: " + payable.getPayableId()));
                    }
                })
                .onErrorResume(IllegalArgumentException.class, e -> {throw e;});
    }


    @Override
    public Mono<Void> deletePayable(String payableId) {
        this.logger.info(String.format("%1$s(%2$s)", "deletePayable", payableId));

        if(payableId == null) {
            return Mono.error(new BaseHttpException("No id provided.", payableId));
        }

        return this.repository.findById(payableId)
                .doOnNext(payable -> {
                    this.logger.info("Attempting to delete: " + payableId);
                    this.logger.info(String.valueOf(payable));
                })
                .flatMap(
                        x -> this.repository.delete(x)
                )
                .onErrorMap(
                        x -> {
                            this.logger.info(String.valueOf(x));
                            return new EntityDoesNotExistHttpException("No Field with id exists: " + payableId, "");
                        }
                );
    }
}

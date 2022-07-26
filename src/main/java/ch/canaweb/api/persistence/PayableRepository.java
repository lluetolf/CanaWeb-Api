package ch.canaweb.api.persistence;

import ch.canaweb.api.core.Payable.Payable;
import com.google.cloud.Timestamp;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PayableRepository extends FirestoreReactiveRepository<Payable> {
    Mono<Payable> findByPayableId(String id);

    Flux<Payable> findAll();

    Mono<Payable> save(Payable payable);

    Mono<Void> delete(Payable payable);

    Mono<Void> deleteById(String payableId);

    Flux<Payable> findByTransactionDateGreaterThanEqualAndTransactionDateLessThanEqual(Timestamp from, Timestamp until);
}

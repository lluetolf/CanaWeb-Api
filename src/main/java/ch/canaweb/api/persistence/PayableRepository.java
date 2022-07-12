package ch.canaweb.api.persistence;

import ch.canaweb.api.core.Field.Field;
import ch.canaweb.api.core.Payable.Payable;
import com.google.cloud.Timestamp;
import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PayableRepository extends FirestoreReactiveRepository<Payable> {
    Mono<Payable> findById(String id);

    Flux<Payable> findPayablesByTransactionDateBetween(Timestamp from, Timestamp to);

    Flux<Payable> findAll();

    Mono<Field> save(Payable payable);

    Mono<Void> delete(Payable payable);
}

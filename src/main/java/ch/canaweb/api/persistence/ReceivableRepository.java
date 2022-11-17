package ch.canaweb.api.persistence;

import ch.canaweb.api.core.Receivable.Receivable;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import reactor.core.publisher.Flux;

public interface ReceivableRepository extends FirestoreReactiveRepository<Receivable> {
    Flux<Receivable> findAll();
}

package ch.canaweb.api.persistence;

import ch.canaweb.api.core.Field.Field;
import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface FieldRepository extends FirestoreReactiveRepository<Field> {

    Mono<Field> findByFieldId(String fieldId);

    Mono<Field> findById(String id);

    Flux<Field> findAll();

    Mono<Field> save(Field field);

    Mono<Void> deleteByFieldId(int fieldId);
}

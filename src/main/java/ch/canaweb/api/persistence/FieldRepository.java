package ch.canaweb.api.persistence;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import ch.canaweb.api.core.Field.Field;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface FieldRepository extends FirestoreReactiveRepository<Field> {

    Mono<Field> findById(String id);

    Mono<Field> findFieldByName(String name);

    Flux<Field> findAll();

    Mono<Field> save(Field field);

    Mono<Void> delete(Field f);

}

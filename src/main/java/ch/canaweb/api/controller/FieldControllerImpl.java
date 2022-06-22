package ch.canaweb.api.controller;

import ch.canaweb.api.core.Field.Field;
import ch.canaweb.api.core.Field.FieldService;
import ch.canaweb.api.error.BaseHttpException;
import ch.canaweb.api.error.DuplicateHttpException;
import ch.canaweb.api.error.EntityDoesNotExistHttpException;
import ch.canaweb.api.persistence.FieldRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
public class FieldControllerImpl implements FieldService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private FieldRepository repository;

    @Autowired
    public void setRepository(FieldRepository repository){
        this.repository=repository;
    }

    @Override
    public Mono<String> getVersion() {
        return Mono.just("V1.0");
    }

    @Override
    public Mono<Field> getField(String fieldId) {
        this.logger.info("FieldId: " + fieldId);
        return this.repository.findByFieldId(fieldId).log();
    }

    @Override
    public Mono<Field> getFieldByName(String name) {
        this.logger.info("getFieldByName: " + name);
        return this.repository.findFieldByName(name).log()
                .switchIfEmpty( Mono.error(new EntityDoesNotExistHttpException("Entity with Name does not exist: " + name, "")));
    }

    @Override
    public Flux<Field> getAllFields() {
        this.logger.info("getAllFields: ");
        try {
            return this.repository.findAll()
                    .onErrorMap(
                    x -> {
                        this.logger.error("ERROR:" + String.valueOf(x));
                        return new BaseHttpException(x.getMessage(), "");
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Mono<Field> createField(Field field) {
        this.logger.info("createField: " + field.getFieldId());

        Mono<Field> m = this.repository.findFieldByName(field.getName())
                .hasElement()
                .flatMap(x -> {
                    if (x) {
                        this.logger.info("Field exists already.");
                        return Mono.error(new DuplicateHttpException("There is already a Field with name: " + field.getName(), "no details"));
                    } else {
                        this.logger.info("Create Field.");
                        return this.repository.save(field);
                    }
                });

        return m;

    }

    @Override
    public Mono<Field> updateField(Field field) {
        return this.repository.findByFieldId(field.getFieldId())
                .flatMap(a -> {
                    return this.repository.save(field);
                })
                .onErrorResume(IllegalArgumentException.class, e -> {throw e;});
    }

    @Override
    public Mono<Void> deleteField(String fieldId) {
        this.logger.info("deleteField: " + fieldId);

        Mono<Void> r =  this.repository.findByFieldId(fieldId)
                .doOnNext(x -> {
                    this.logger.info("Attempting to delete: " + fieldId);
                    this.logger.info(String.valueOf(x));
                })
                .flatMap(
                        x -> {
                            return this.repository.delete(x);
                        }
                )
                .onErrorMap(
                x -> {
                    this.logger.info(String.valueOf(x));
                    return new EntityDoesNotExistHttpException("No Field with id exists: " + fieldId, "");
                }
        );

        return r;
    }

//    @ExceptionHandler(NullPointerException.class)
//    public ResponseEntity<String> onException(NullPointerException e) {
//        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
//    }
}

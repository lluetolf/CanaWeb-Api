package ch.canaweb.api.controller;

import ch.canaweb.api.core.Field.Field;
import ch.canaweb.api.core.Field.FieldService;
import ch.canaweb.api.error.BaseHttpException;
import ch.canaweb.api.error.DuplicateHttpException;
import ch.canaweb.api.error.EntityDoesNotExistHttpException;
import ch.canaweb.api.persistence.FieldRepository;
import com.google.cloud.Timestamp;
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
        return Mono.just("V1.1");
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
                        this.logger.error("ERROR:" + x.toString());
                        return new BaseHttpException(x.getMessage(), "");
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Mono<Field> createField(Field field) {
        this.logger.info(String.format("%1$s(%2$s)", "createField", field.getName()));

        try {
            return this.repository.findFieldByName(field.getName())
                    .hasElement()
                    .flatMap(x -> {
                        if (x) {
                            this.logger.info("Field exists already.");
                            return Mono.error(new DuplicateHttpException("There is already a Field with name: " + field.getName(), "no details"));
                        } else {
                            this.logger.info("Create Field.");
                            field.setLastUpdated(Timestamp.now());
                            return this.repository.save(field);
                        }
                    })
                    .onErrorMap( IllegalArgumentException.class, e -> new BaseHttpException("Failed to create new Field", e.getMessage()) );
        } catch (Exception e) {
            this.logger.error("ERROR CAUGHT: "  + e.getMessage());
            e.printStackTrace();
        }

        return Mono.error(new BaseHttpException("Error", "failed"));
    }

    @Override
    public Mono<Field> updateField(Field field, String fieldName) {
        this.logger.info(String.format("%1$s(%2$s, %3$s)", "updateField", field.getName(), fieldName));

        if(fieldName != null) {
            if(!fieldName.equals(field.getName()))
                return Mono.error(new BaseHttpException("RequestParam name does not match payload.", fieldName));
        }

        return this.repository.findFieldByName(field.getName())
                .doOnNext( x -> field.setId(x.getId()))
                .hasElement()
                .flatMap(x -> {
                    if(x) {
                        field.setLastUpdated(Timestamp.now());
                        return this.repository.save(field);
                    } else {
                        this.logger.info("Can't update field for none existing id: " + field.getId());
                        return Mono.error(new BaseHttpException("Can't update field for none existing id.", "ID: " + field.getId()));
                    }
                })
                .onErrorResume(IllegalArgumentException.class, e -> {throw e;});
    }

    @Override
    public Mono<Void> deleteField(String fieldName) {
        this.logger.info("deleteField: " + fieldName);

        return this.repository.findFieldByName(fieldName)
                .doOnNext(x -> {
                    this.logger.info("Attempting to delete: " + fieldName);
                    this.logger.info(String.valueOf(x));
                })
                .flatMap(
                        x -> this.repository.delete(x)
                )
                .onErrorMap(
                x -> {
                    this.logger.info(String.valueOf(x));
                    return new EntityDoesNotExistHttpException("No Field with id exists: " + fieldName, "");
                }
        );
    }

}

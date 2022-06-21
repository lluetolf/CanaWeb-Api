package ch.canaweb.api.controller;

import ch.canaweb.api.core.Field.Field;
import ch.canaweb.api.core.Field.FieldService;
import ch.canaweb.api.persistence.FieldRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@RestController
public class FieldControllerImpl implements FieldService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final FieldRepository repository;

    @Autowired
    public FieldControllerImpl(FieldRepository repository) {
        this.repository = repository;
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
        return null;
    }

    @Override
    public Flux<Field> getAllFields() {
        try {
            return this.repository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Mono<Field> createField(Field field) {
        return this.repository.findByFieldId(field.getFieldId())
                .log()
                .flatMap(a -> {
                    if(a == null) {
                        this.logger.info("Creating new field. ID:" + field.getFieldId());
                        return this.repository.save(field);
                    } else {
                        this.logger.info("Field already exists, consider updating. ID: " + field.getFieldId());
                        return Mono.error(new Exception("No Field with FieldID" + field.getFieldId()));
                    }
                }).log();
    }

    @Override
    public Mono<Field> updateField(Field field) {
        return this.repository.findByFieldId(field.getFieldId())
                .flatMap(a -> {
                    field.setId(a.getId());
                    return this.repository.save(field);
                })
                .onErrorResume(IllegalArgumentException.class, e -> {throw e;});
    }

    @Override
    public Mono<Void> deleteField(int fieldId) {
        return null;
    }
}

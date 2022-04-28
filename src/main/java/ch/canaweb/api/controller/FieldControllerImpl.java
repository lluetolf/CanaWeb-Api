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


    @GetMapping("/hello")
    public Mono<String> hello() {
        return Mono.just("Hello World 3.");
    }

    @Override
    public Mono<String> getVersion() {
        return Mono.just("V1.0");
    }

    @Override
    public Mono<Field> getField(String fieldId) {
        Field field = this.repository.getField(fieldId);
        return Mono.just(field);
    }

    @Override
    public Mono<Field> getFieldByName(String name) {
        return null;
    }

    @Override
    public Flux<Field> getAllFields() {
        List<Field> fields = this.repository.getAllFields();
        return Flux.fromIterable(fields);
    }

    @Override
    public Mono<Field> createField(Field field) {
        this.repository.add(field);
        return Mono.just(field);
    }

    @Override
    public Mono<Field> updateField(Field body) {
        return null;
    }

    @Override
    public Mono<Void> deleteField(int fieldId) {
        return null;
    }
}

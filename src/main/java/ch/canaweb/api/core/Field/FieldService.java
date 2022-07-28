package ch.canaweb.api.core.Field;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/api/field")
public interface FieldService {

    @GetMapping(
            path = "/version",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Mono<String> getVersion();

    @GetMapping(
            path = "/all",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Flux<Field> getAllFields();

    @GetMapping(
            path = "/name/{name}",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Mono<Field> getFieldByName(@PathVariable String name);

    @PostMapping(
            path = "",
            consumes = "application/json",
            produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    Mono<Field> createField(
            @Validated
            @RequestBody Field body);

    @PutMapping(
            path = "",
            consumes = "application/json",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Mono<Field> updateField(
            @Validated
            @RequestBody Field field,
            @RequestParam( name = "fieldName", required = false) String fieldName);

    @DeleteMapping(
            path = "/name/{fieldName}")
    @ResponseStatus(HttpStatus.OK)
    Mono<Void> deleteField(@PathVariable String fieldName);
}

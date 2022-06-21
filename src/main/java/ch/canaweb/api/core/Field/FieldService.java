package ch.canaweb.api.core.Field;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/field")
public interface FieldService {

    @GetMapping(
            path = "/version",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Mono<String> getVersion();

    @GetMapping(
            path = "",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Mono<Field> getField(@RequestParam(required = false) String fieldId);

    @GetMapping(
            path = "/name/{name}",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Mono<Field> getFieldByName(@PathVariable String name);

    @GetMapping(
            path = "/all",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Flux<Field> getAllFields();

    @PostMapping(
            path = "",
            consumes = "application/json",
            produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    Mono<Field> createField(@RequestBody Field body);

    @PatchMapping(
            path = "",
            consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Mono<Field> updateField(@RequestBody Field body);

    @DeleteMapping(
            path = "/{fieldId}")
    @ResponseStatus(HttpStatus.OK)
    Mono<Void> deleteField(@PathVariable int fieldId);
}

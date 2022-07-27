package ch.canaweb.api.core.Payable;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/api/payable")
public interface PayableService {

    @GetMapping(
            path = "/version",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Mono<String> getVersion();

    @GetMapping(
            value    = "",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Mono<Payable> getPayable(@RequestParam() String payableId);

    @GetMapping(
            value    = "/all",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Flux<Payable> getAllPayables();

    @GetMapping(
            value    = "/between",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Mono<List<Payable>>  getAllPayablesBetween(
            @RequestParam(name = "from", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(name = "until", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate until
    );

    @PostMapping(
            path = "",
            consumes = "application/json",
            produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    Mono<Payable> createPayable(
            @Validated
            @RequestBody Payable body);

    @PutMapping(
            path = "",
            consumes = "application/json",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Mono<Payable> updatePayable(
            @Validated
            @RequestBody Payable body,
            @RequestParam(required = false) String payableId
    );

    @DeleteMapping(
            path = "/{payableId}")
    @ResponseStatus(HttpStatus.OK)
    Mono<Void> deletePayable(@PathVariable String payableId);
}

package ch.canaweb.api.core.Payable;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    Mono<Payable> getPayable(@RequestParam(required = true) String payableId);

    @GetMapping(
            value    = "/all",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Flux<Payable> getAllPayables();

    @GetMapping(
            value    = "/between",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Flux<Payable> getAllPayablesBetween(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    );

    @PostMapping(
            path = "",
            consumes = "application/json",
            produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    Mono<Payable> createPayable(@RequestBody Payable body);

    @PutMapping(
            path = "",
            consumes = "application/json",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Mono<Payable> updatePayable(@RequestBody Payable body);

    @DeleteMapping(
            path = "/payable/{payableId}")
    @ResponseStatus(HttpStatus.OK)
    Mono<Void> deletePayable(@PathVariable int payableId);
}

package ch.canaweb.api.core.Receivable;

import ch.canaweb.api.core.Payable.Payable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/api/receivable")
public interface ReceivableService {

    @GetMapping(
            value = "/all",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Flux<Receivable> getAllReceivables();

    @PutMapping(
            path = "",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Mono<Receivable> updateDeductible(
            @Validated
            @RequestParam(required = true) String receivableId,
            @RequestParam(required = true) String phase,
            @RequestParam(required = true) double amount
    );
}

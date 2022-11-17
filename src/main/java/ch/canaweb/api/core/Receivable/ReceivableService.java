package ch.canaweb.api.core.Receivable;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RequestMapping("/api/receivable")
public interface ReceivableService {

    @GetMapping(
            value = "/all",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Flux<Receivable> getAllReceivables();

}

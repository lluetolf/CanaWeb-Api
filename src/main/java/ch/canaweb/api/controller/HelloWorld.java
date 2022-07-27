package ch.canaweb.api.controller;

import ch.canaweb.api.error.BaseHttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;


@RestController
public class HelloWorld {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/hello")
    public Mono<Map> hello() {
        return Mono.just(
                Map.of("version", "V1.0", "time", LocalDateTime.of(2022, 07, 27, 10, 20).toString())
        );
    }

    @GetMapping(path = "/user")
    public Mono<String>  test(Principal principal) {
        if(principal != null) {
            return Mono.just(principal.getName());
        } else {
            return Mono.error(new BaseHttpException("Security Principle not set.", "No details"));
        }
    }

}

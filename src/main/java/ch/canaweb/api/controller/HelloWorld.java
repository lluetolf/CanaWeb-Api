package ch.canaweb.api.controller;

import ch.canaweb.api.error.BaseHttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;


@RestController
public class HelloWorld {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/hello")
    public Mono<String> hello() {
        return Mono.just("Hello World 3.");
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

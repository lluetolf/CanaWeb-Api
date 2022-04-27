package ch.canaweb.api.controller;

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
    public String test(Principal principal) {
        return principal.getName();
    }

}

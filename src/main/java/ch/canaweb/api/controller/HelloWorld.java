package ch.canaweb.api.controller;

import ch.canaweb.api.error.BaseHttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;


@RestController
public class HelloWorld {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment env;

    @GetMapping("/hello")
    public Mono<Map> hello() {
        return Mono.just(
                Map.of(
                        "version", "V1.0",
                        "time", env.getProperty("app.build-time"),
                        "commit", env.getProperty("app.commit-sha"),
                        "branch", env.getProperty("app.branch-name"))
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

package ch.canaweb.api.services;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

    @Profile({"prod", "uat", "dev"})
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    SecurityWebFilterChain apiHttpSecurity(ServerHttpSecurity http) {
        return http.securityMatcher(new PathPatternParserServerWebExchangeMatcher("/api/**"))
                .authorizeExchange().anyExchange().authenticated()
                .and().oauth2ResourceServer().jwt().and().and().build();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .cors().configurationSource(corsConfiguration())
                .and().csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfiguration() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("http://localhost:4200");
        corsConfig.addAllowedOrigin("https://www.canaweb.app");
        corsConfig.addAllowedOrigin("https://canaweb.app");
        corsConfig.addAllowedOrigin("https://canaweb-ui-dot-dev-canaweb-firestore.uc.r.appspot.com");
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        corsConfig.setMaxAge(3600L);
        corsConfig.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

}
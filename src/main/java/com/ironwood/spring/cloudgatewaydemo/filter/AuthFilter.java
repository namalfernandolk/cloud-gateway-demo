package com.ironwood.spring.cloudgatewaydemo.filter;

import jakarta.ws.rs.core.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private WebClient webClient;

    public AuthFilter() {
        super(Config.class);
    }
    @Autowired
    public AuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Config newConfig() {
        return new Config();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
                throw new RuntimeException("missing authorization header");

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith("Bearer "))
                authHeader = authHeader.substring(7);

            String expectedRole = config.role;

            return this.webClient
                    .get()
                    .uri("http://mtokens-authentication-service/auth/validate?token=" + authHeader + "&role=" + expectedRole)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .flatMap(response -> {
                        exchange.getResponse().getHeaders().add("Custom-Header", "Custom-Value");
                        return chain.filter(exchange);
                    })
                    .onErrorResume(throwable -> {
                        throw new RuntimeException("un authorized access to application");
                    });
        };
    }
    public static class Config {
        private String role;
        public String getRole() {
            return role;
        }
        public void setRole(String role) {
            this.role = role;
        }
    }
}

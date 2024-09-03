package com.news.api_gateway.configuration;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.news.api_gateway.dto.ApiResponse;
import com.news.api_gateway.service.AuthService;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
@Slf4j
@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {
    @Autowired
    AuthService authService;
    @Autowired
    ObjectMapper objectMapper;
    @NonFinal
    private String[] publicEndpoint = {
            "/auth/**"
    };
    @NonFinal
    @Value("${app.api-prefix}")
    private String apiPrefix;
    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (isPublicEndpoint(exchange.getRequest()))
            return chain.filter(exchange);
        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty(authHeader)) {
            return unauthenticated(exchange.getResponse());
        }
        String token = authHeader.getFirst().replace("Bearer ", "");
        return authService.authenticate(token).flatMap(authenticate -> {
            if (authenticate.getData().isValid()) {
                return chain.filter(exchange);
            }
            else {
                return unauthenticated(exchange.getResponse());
            }
        }).onErrorResume(throwable -> unauthenticated(exchange.getResponse()));
    }

    @Override
    public int getOrder() {
        return -1;
    }

    Mono<Void> unauthenticated(ServerHttpResponse response){
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(1041)
                .build();
        String body;
        try{
            body = objectMapper.writeValueAsString(apiResponse);

        } catch (JsonProcessingException e) {
            log.error("Error serializing response", e);
            body = "{\"code\":1401,\"message\":\"Unauthenticated\"}";
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(body.getBytes()))
        );
    }

    private boolean isPublicEndpoint(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        for(String endpoint : publicEndpoint) {
            if(matcher.match(apiPrefix + endpoint, path))
                return true;
        }
        return false;
    }

}

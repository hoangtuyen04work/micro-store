package com.news.api_gateway.repository;

import com.news.api_gateway.dto.ApiResponse;
import com.news.api_gateway.dto.AuthRequest;
import com.news.api_gateway.dto.AuthResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface AuthClient {
    @PostExchange(url="/authenticate", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ApiResponse<AuthResponse>> authenticate(@RequestBody AuthRequest authRequest);
}

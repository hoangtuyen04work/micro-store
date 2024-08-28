package com.news.api_gateway.service;

import com.news.api_gateway.dto.ApiResponse;
import com.news.api_gateway.dto.AuthRequest;
import com.news.api_gateway.dto.AuthResponse;
import com.news.api_gateway.repository.AuthClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    AuthClient authClient;

    public Mono<ApiResponse<AuthResponse>>  authenticate(String token){
        return authClient.authenticate(AuthRequest.builder()
                        .token(token)
                        .build());
    }
}

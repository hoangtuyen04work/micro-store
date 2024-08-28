package com.shop.auth_service.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String refreshToken;
    private boolean isValid = true;
}

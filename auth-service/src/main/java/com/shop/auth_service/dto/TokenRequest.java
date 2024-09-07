package com.shop.auth_service.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequest {
    private String token;
    private String refreshToken;
}

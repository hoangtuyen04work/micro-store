package com.shop.auth_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthRequest {
    private String phoneNumber;
    private String email;
    private String password;
    private String token;
    private String refreshToken;
    private String userName;
}

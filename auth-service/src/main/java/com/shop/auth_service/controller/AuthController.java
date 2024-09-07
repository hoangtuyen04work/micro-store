package com.shop.auth_service.controller;

import com.nimbusds.jose.JOSEException;
import com.shop.auth_service.dto.*;
import com.shop.auth_service.exception.AppException;
import com.shop.auth_service.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;

    @PostMapping("/refreshToken")
    public ApiResponse<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshToken) throws AppException, ParseException, JOSEException {
        return  ApiResponse.<AuthResponse>builder()
                .data(authService.refreshToken(refreshToken))
                .build();
    }
    @PostMapping("/authenticate")
    public ApiResponse<Boolean> authenticate(@RequestBody AuthRequest authRequest) throws AppException {
        return ApiResponse.<Boolean>builder()
                .data(authService.authenticate(authRequest))
                .build();
    }
    @PostMapping("/signup/phone")
    public ApiResponse<AuthResponse> signupByPhoneNumber(@RequestBody AuthRequest authRequest) throws AppException, ParseException, JOSEException {
        return ApiResponse.<AuthResponse>builder()
                .data(authService.signupWithPhone(authRequest))
                .build();
    }
    @PostMapping("/signup/email")
    public ApiResponse<AuthResponse> signupByEmail(@RequestBody AuthRequest authRequest) throws AppException, JOSEException {
        return ApiResponse.<AuthResponse>builder()
                .data(authService.signupWithEmail(authRequest))
                .build();
    }
    @PostMapping("/login/phone")
    public ApiResponse<AuthResponse> loginByPhoneNumber(@RequestBody AuthRequest authRequest) throws AppException, JOSEException {
        return ApiResponse.<AuthResponse>builder()
                .data(authService.loginWithPhone(authRequest))
                .build();
    }
    @PostMapping("/login/email")
    public ApiResponse<AuthResponse> loginByPhoneEmail(@RequestBody AuthRequest authRequest) throws AppException, ParseException, JOSEException {
        return ApiResponse.<AuthResponse>builder()
                .data(authService.loginWithEmail(authRequest))
                .build();
    }
    @PostMapping("/logoutt")
    public ApiResponse<Boolean> logout(@RequestBody TokenRequest request) {
        authService.logout(request);
        return ApiResponse.<Boolean>builder()
                .data(true)
                .build();
    }
}

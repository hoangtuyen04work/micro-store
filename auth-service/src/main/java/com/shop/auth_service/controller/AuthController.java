package com.shop.auth_service.controller;

import com.nimbusds.jose.JOSEException;
import com.shop.auth_service.dto.ApiResponse;
import com.shop.auth_service.dto.AuthRequest;
import com.shop.auth_service.dto.AuthResponse;
import com.shop.auth_service.exception.AppException;
import com.shop.auth_service.service.AuthService;
import com.shop.auth_service.service.UserService;
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

    @PostMapping("/authenticate")
    public ApiResponse<AuthResponse> authenticate(@RequestBody AuthRequest authRequest) throws AppException, ParseException, JOSEException {
        return ApiResponse.<AuthResponse>builder()
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
    public ApiResponse<AuthResponse> signupByEmail(@RequestBody AuthRequest authRequest) throws AppException, ParseException, JOSEException {
        return ApiResponse.<AuthResponse>builder()
                .data(authService.signupWithEmail(authRequest))
                .build();
    }
    @PostMapping("/login/phone")
    public ApiResponse<AuthResponse> loginByPhoneNumber(@RequestBody AuthRequest authRequest) throws AppException, ParseException, JOSEException {
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
    @PostMapping("/logout")
    public ApiResponse<Boolean> logout(@RequestBody AuthRequest authRequest) throws AppException, ParseException, JOSEException {
        authService.logout(authRequest);
        return ApiResponse.<Boolean>builder()
                .data(true)
                .build();
    }
}

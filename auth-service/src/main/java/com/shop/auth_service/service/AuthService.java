package com.shop.auth_service.service;

import com.nimbusds.jose.JOSEException;
import com.shop.auth_service.dto.*;
import com.shop.auth_service.exception.AppException;
import com.shop.auth_service.exception.ErrorCode;
import com.shop.auth_service.repository.httpclient.NotificationClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class AuthService {
    TokenService tokenService;
    UserService userService;
    NotificationClient notificationClient;
    public AuthResponse authenticate(AuthRequest authRequest) throws AppException, ParseException, JOSEException {
        tokenService.checkToken(authRequest.getToken());
        return AuthResponse.builder()
                .token(authRequest.getToken())
                .isValid(true)
                .build();
    }
    public void logout(AuthRequest authRequest) throws AppException, ParseException, JOSEException {
        tokenService.disableToken(authRequest);
    }
    public AuthResponse loginWithEmail(AuthRequest authRequest) throws AppException, ParseException, JOSEException {
        if(!userService.existsByEmail(authRequest.getEmail()))
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        UserResponse user = userService.getUserByEmail(authRequest.getEmail(), authRequest.getPassword());
        return AuthResponse.builder()
                .refreshToken("refresh")
                .token(tokenService.generateToken(user, false))
                .build();
    }
    public AuthResponse loginWithPhone(AuthRequest authRequest) throws AppException, ParseException, JOSEException {
        if(!userService.existsByPhoneNumber(authRequest.getPhoneNumber()))
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        UserResponse user = userService.getUserByPhone(authRequest.getPhoneNumber(), authRequest.getPassword());
        return AuthResponse.builder()
                .refreshToken("refresh")
                .token(tokenService.generateToken(user, false))
                .build();
    }
    public AuthResponse signupWithEmail(AuthRequest authRequest) throws AppException, ParseException, JOSEException {
        if(userService.existsByEmail(authRequest.getEmail()))
            throw new AppException(ErrorCode.USER_EXISTED);
        UserResponse user = userService.createUserByEmail(authRequest.getUserName(), authRequest.getEmail(), authRequest.getPassword());
        String token = tokenService.generateToken(user, false);
        notificationClient.emailWelcome(notification(authRequest.getEmail()));
        return AuthResponse.builder()
                .refreshToken("refresh")
                .token(token)
                .build();
    }
    public AuthResponse signupWithPhone(AuthRequest authRequest) throws AppException, ParseException, JOSEException {
        if(userService.existsByPhoneNumber(authRequest.getPhoneNumber()))
            throw new AppException(ErrorCode.USER_EXISTED);
        UserResponse user = userService.createUserByPhone(authRequest.getUserName(), authRequest.getPhoneNumber(), authRequest.getPassword());
        return AuthResponse.builder()
                .refreshToken("refresh")
                .token(tokenService.generateToken(user, false))
                .build();
    }
    public NotificationRequest notification(String email){
        return NotificationRequest.builder()
                .to(EmailObject.builder()
                        .email(email)
                        .name("HTShop")
                        .build())
                .htmlContent("<h1>Hello, welcome to our Store, wish you have a nice day!!!</h1>")
                .subject("Welcome to HTStore")
                .build();
    }
}


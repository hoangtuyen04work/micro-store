package com.shop.auth_service.service;

import com.nimbusds.jose.JOSEException;
import com.shop.auth_service.dto.*;
import com.shop.auth_service.entity.Token;
import com.shop.auth_service.exception.AppException;
import com.shop.auth_service.exception.ErrorCode;
import com.shop.event.SendEmailRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class AuthService {
    TokenService tokenService;
    UserService userService;
    KafkaTemplate<String, SendEmailRequest> kafkaTemplate;

    public boolean authenticate(AuthRequest authRequest) throws AppException {
        tokenService.checkToken(authRequest.getToken());
        return tokenService.isTokenValid(authRequest.getToken());
    }
    public AuthResponse generateAuthResponse(UserResponse userResponse) throws JOSEException {
        Token token = tokenService.generateToken(userResponse);
        tokenService.save(token);
        return AuthResponse.builder()
                .refreshToken(token.getRefreshToken())
                .token(token.getToken())
                .build();
    }
    public AuthResponse refreshToken(RefreshTokenRequest request) throws AppException, ParseException, JOSEException {
        boolean isOk = tokenService.isRefreshTokenValid(request.getRefreshToken());
        if(!isOk){
            throw new AppException(ErrorCode.REFRESH_TOKEN_INVALID);
        }
        Token token = tokenService.getTokenByRefreshToken(request.getRefreshToken());
        tokenService.deleteToken(token);
        return generateAuthResponse(userService.getUserByToken(token.getToken()));
    }
    public void logout(TokenRequest request) {
        tokenService.deleteToken(request);
    }
    public AuthResponse loginWithEmail(AuthRequest authRequest) throws AppException, ParseException, JOSEException {
        if(!userService.existsByEmail(authRequest.getEmail()))
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        UserResponse user = userService.getUserByEmail(authRequest.getEmail(), authRequest.getPassword());
        return generateAuthResponse(user);
    }
    public AuthResponse loginWithPhone(AuthRequest authRequest) throws AppException, JOSEException {
        if(!userService.existsByPhoneNumber(authRequest.getPhoneNumber()))
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        UserResponse user = userService.getUserByPhone(authRequest.getPhoneNumber(), authRequest.getPassword());
        return generateAuthResponse(user);
    }
    public AuthResponse signupWithEmail(AuthRequest authRequest) throws AppException, JOSEException {
        if(userService.existsByEmail(authRequest.getEmail()))
            throw new AppException(ErrorCode.USER_EXISTED);
        kafkaTemplate.send("auth-signup", notification(authRequest.getEmail()));
        UserResponse user = userService.createUserByEmail(authRequest.getUserName(), authRequest.getEmail(), authRequest.getPassword());
        return generateAuthResponse(user);
    }
    public AuthResponse signupWithPhone(AuthRequest authRequest) throws AppException, ParseException, JOSEException {
        if(userService.existsByPhoneNumber(authRequest.getPhoneNumber()))
            throw new AppException(ErrorCode.USER_EXISTED);
        UserResponse user = userService.createUserByPhone(authRequest.getUserName(), authRequest.getPhoneNumber(), authRequest.getPassword());
        return generateAuthResponse(user);
    }
    public SendEmailRequest notification(String email){
        return SendEmailRequest.builder()
                .to(EmailObject.builder()
                        .email(email)
                        .name("HTShop")
                        .build())
                .htmlContent("<h1>Hello, welcome to our Store, wish you have a nice day!!!</h1>")
                .subject("Welcome to HTStore")
                .build();
    }
}


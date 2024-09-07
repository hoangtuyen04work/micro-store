package com.shop.auth_service.service;

import com.shop.auth_service.constant.Constant;
import com.shop.auth_service.dto.UserResponse;
import com.shop.auth_service.entity.User;
import com.shop.auth_service.exception.AppException;
import com.shop.auth_service.exception.ErrorCode;
import com.shop.auth_service.repository.UserRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Collections;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class UserService {
    PasswordEncoder passwordEncoder;
    UserRepo userRepo;
    RoleService roleService;
    TokenService tokenService;
    public UserResponse getUserByToken(String token) throws ParseException, AppException {
        return toResponse(userRepo.findById(tokenService.getUserIdByToken(token)).orElseThrow());
    }
    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }
    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepo.existsByPhoneNumber(phoneNumber);
    }
    public UserResponse createUserByEmail(String userName, String email, String password) throws AppException {
        User user = User.builder()
                .email(email)
                .userName(userName)
                .password(passwordEncoder.encode(password))
                .roles(roleService.getRoles(Collections.singleton(Constant.USER.name())))
                .build();
        userRepo.save(user);
        return toResponse(user);
    }
    public UserResponse createUserByPhone(String userName, String phoneNumber, String password) throws AppException {
        User user = User.builder()
                .phoneNumber(phoneNumber)
                .userName(userName)
                .password(passwordEncoder.encode(password))
                .roles(roleService.getRoles(Collections.singleton(Constant.USER.name())))
                .build();
        userRepo.save(user);
        return toResponse(user);
    }
    public UserResponse getUserByEmail(String email, String password) throws AppException {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
            if(!passwordEncoder.matches(password, user.getPassword())) {
                throw new AppException(ErrorCode.NOT_AUTHENTICATED);
            }
        return toResponse(user);
    }
    public UserResponse getUserByPhone(String phone, String password) throws AppException {
        User user = userRepo.findByPhoneNumber(phone).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new AppException(ErrorCode.NOT_AUTHENTICATED);
        }
        return toResponse(user);
    }
    public UserResponse toResponse(User user) throws AppException {
        return UserResponse.builder()
                .userName(user.getUserName())
                .id(user.getId())
                .email(user.getEmail())
                .roles(user.getRoles())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}

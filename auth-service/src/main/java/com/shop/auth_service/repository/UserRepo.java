package com.shop.auth_service.repository;

import com.shop.auth_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumberAndPassword(String phoneNumber, String password);
    boolean existsByEmailAndPassword(String email, String password);
}

package com.shop.auth_service.repository;

import com.shop.auth_service.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepo extends JpaRepository<Token, Long> {
    boolean existsByToken(String token);
}

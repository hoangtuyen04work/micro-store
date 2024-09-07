package com.shop.auth_service.repository;

import com.shop.auth_service.entity.Token;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TokenRepo extends JpaRepository<Token, Long> {
    boolean existsByToken(String token);
    @Query("SELECT COUNT(t) > 0 FROM Token t WHERE t.refreshToken = :refreshToken")
    boolean existByRefreshToken(@Param("refreshToken") String refreshToken);
    @Query("SELECT t FROM Token t WHERE t.refreshToken = :refreshToken")
    Token findByRefreshToken(@Param("refreshToken") String refreshToken);
    @Modifying
    @Transactional
    @Query("DELETE  FROM Token e WHERE e.token = :token")
    void deleteByToken(@Param("token")String token);
}

package com.shop.auth_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.sql.Timestamp;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String refreshToken;
    private Boolean isActive = true;
    private Timestamp expiry_time;
    private String userId;
}

package com.shop.auth_service.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(columnDefinition = "TEXT")
    private String token;
    private String refreshToken;
    private Date expiry_time;
    private Date refresh_expiry_time;
}

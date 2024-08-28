package com.shop.auth_service.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    private String id;
    private String token;
}

package com.shop.auth_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String roleName;
    @ManyToMany(mappedBy = "roles", cascade = CascadeType.REMOVE)
    Set<User> users;
}

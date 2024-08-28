package com.shop.auth_service.entity;


import com.shop.auth_service.constant.Constant;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy =  GenerationType.UUID)
    private String id;
    private String email;
    private String userName;
    private String phoneNumber;
    private String password;
    @Enumerated(EnumType.STRING)  // Ensures the enum is stored as a string in the database
    private Constant state;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

}

package com.shop.auth_service.dto;

import com.shop.auth_service.entity.Role;
import lombok.*;

import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String email;
    private String phoneNumber;
    private Set<Role> roles;
    private String userName;
}

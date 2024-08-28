package com.shop.auth_service.service;

import com.shop.auth_service.entity.Role;
import com.shop.auth_service.exception.AppException;
import com.shop.auth_service.exception.ErrorCode;
import com.shop.auth_service.repository.RoleRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class RoleService {
    RoleRepo roleRepo;

    public Role getRole(String roleName) throws AppException {
        return roleRepo.getRoleByRoleName(roleName)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
    }
    public Set<Role> getRoles(Set<String> roleNames) {
        return roleNames.stream()
                .map(roleName -> roleRepo.getRoleByRoleName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());
    }

}

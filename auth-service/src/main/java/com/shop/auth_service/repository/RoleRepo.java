package com.shop.auth_service.repository;

import com.shop.auth_service.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, String> {
    Optional<Role> getRoleByRoleName(String roleName);
    boolean existsByRoleName(String roleName);
}

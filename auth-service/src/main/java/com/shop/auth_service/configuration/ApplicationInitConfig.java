package com.shop.auth_service.configuration;

import com.shop.auth_service.constant.Constant;
import com.shop.auth_service.entity.Role;
import com.shop.auth_service.entity.User;
import com.shop.auth_service.repository.RoleRepo;
import com.shop.auth_service.repository.UserRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepo userRepo, RoleRepo roleRepo) {
        return args -> {
            Role userRole = Role.builder()
                    .roleName(String.valueOf(Constant.USER))
                    .build();
            if(!roleRepo.existsByRoleName(String.valueOf(Constant.USER))){
                userRole = roleRepo.save(userRole);
            }
            Role adminRole = Role.builder()
                    .roleName(String.valueOf(Constant.ADMIN))
                    .build();
            if(!roleRepo.existsByRoleName(String.valueOf(Constant.ADMIN))){
                adminRole = roleRepo.save(adminRole);
            }
            if(!userRepo.existsByEmail(Constant.ADMIN + "@gmail.com")){
                var roles = new HashSet<Role>();
                roles.add(userRole);
                roles.add(adminRole);
                User user = User.builder()
                        .email(Constant.ADMIN + "@gmail.com")
                        .phoneNumber(String.valueOf(Constant.ADMIN))
                        .password(passwordEncoder.encode(String.valueOf(Constant.ADMIN)))
                        .roles(roles)
                        .build();
                userRepo.save(user);
            }

        };
    }
}

package com.example.security;

import com.example.security.model.UserEntity;
import com.example.security.model.UserRoleEntity;
import com.example.security.model.enums.UserRole;
import com.example.security.repo.UserRepo;
import com.example.security.repo.UserRoleRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SecurityApplicationInit implements CommandLineRunner {
    private final UserRepo userRepo;
    private final UserRoleRepo userRoleRepo;
    private final PasswordEncoder passwordEncoder;

    public SecurityApplicationInit(UserRepo userRepo, UserRoleRepo userRoleRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.userRoleRepo = userRoleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Init Roles (ADMIN, USER) in DB
        UserRoleEntity userRole = userRoleRepo.save(new UserRoleEntity().setRole(UserRole.USER));
        UserRoleEntity adminRole = userRoleRepo.save(new UserRoleEntity().setRole(UserRole.ADMIN));
        // Init UserEntities (user, admin) in DB
        final UserEntity userEntity = new UserEntity();
        userEntity.setName("user").setPassword(passwordEncoder.encode("123")).setRoles(List.of(userRole));
        final UserEntity adminEntity = new UserEntity();
        adminEntity.setName("admin").setPassword(passwordEncoder.encode("123")).setRoles(List.of(adminRole, userRole));

        userRepo.saveAll(List.of(userEntity, adminEntity));
    }
}

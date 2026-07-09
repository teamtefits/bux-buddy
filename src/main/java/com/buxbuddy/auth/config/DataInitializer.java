package com.buxbuddy.auth.config;

import com.buxbuddy.auth.entity.Role;
import com.buxbuddy.auth.enums.RoleType;
import com.buxbuddy.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            roleRepository.save(Role.builder().name(RoleType.ADMIN).build());
            roleRepository.save(Role.builder().name(RoleType.BUSINESS_OWNER).build());
            roleRepository.save(Role.builder().name(RoleType.EMPLOYEE).build());
            roleRepository.save(Role.builder().name(RoleType.CUSTOMER).build());
            System.out.println("✅ Default roles inserted");
        }
    }
}
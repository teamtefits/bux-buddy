package com.buxbuddy.auth.config;

import com.buxbuddy.auth.entity.BusinessCategory;
import com.buxbuddy.auth.entity.Role;
import com.buxbuddy.auth.entity.User;
import com.buxbuddy.auth.enums.BusinessCategoryType;
import com.buxbuddy.auth.enums.RoleType;
import com.buxbuddy.auth.repository.BusinessCategoryRepository;
import com.buxbuddy.auth.repository.RoleRepository;
import com.buxbuddy.auth.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BusinessCategoryRepository businessCategoryRepository;

    @Override
    public void run(String... args) {

        createRoles();

        createAdminUser();
    }


    private void createRoles() {

        if (roleRepository.count() == 0) {

            roleRepository.save(Role.builder()
                    .name(RoleType.ADMIN)
                    .build());
            roleRepository.save(Role.builder()
                    .name(RoleType.BUSINESS_OWNER)
                    .build());
            roleRepository.save(Role.builder()
                    .name(RoleType.EMPLOYEE)
                    .build());
            roleRepository.save(Role.builder()
                    .name(RoleType.CUSTOMER)
                    .build());
            System.out.println("✅ Default roles inserted");
        }
    }


    private void createAdminUser() {

        if (!userRepository.existsByEmail("admin@buxbuddy.com")) {
            Role adminRole = roleRepository.findByName(RoleType.ADMIN)
                    .orElseThrow(() -> new RuntimeException("ADMIN role not found"));
            User admin = User.builder()
                    .firstName("System")
                    .lastName("Admin")
                    .email("admin@buxbuddy.com")
                    .password(passwordEncoder.encode("Admin@123"))
                    .roles(Set.of(adminRole))
                    .enabled(true)
                    .build();
            userRepository.save(admin);
            System.out.println("✅ Default Admin created");
            System.out.println("Email: admin@buxbuddy.com");
            System.out.println("Password: Admin@123");
        }
    }

    @PostConstruct
    public void createBusinessCategories() {

        if (businessCategoryRepository.count() == 0) {
            for (BusinessCategoryType type : BusinessCategoryType.values()) {
                businessCategoryRepository.save(
                        BusinessCategory.builder()
                                .name(type)
                                .build()
                );

            }
            System.out.println("✅ Default business categories inserted");
        }
    }
}

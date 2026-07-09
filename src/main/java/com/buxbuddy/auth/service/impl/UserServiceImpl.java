package com.buxbuddy.auth.service.impl;

import com.buxbuddy.auth.dto.login.RegisterRequest;
import com.buxbuddy.auth.dto.login.RegisterResponse;
import com.buxbuddy.auth.dto.user.UpdateUserRequest;
import com.buxbuddy.auth.dto.user.UserResponse;
import com.buxbuddy.auth.entity.Role;
import com.buxbuddy.auth.entity.User;
import com.buxbuddy.auth.enums.RoleType;
import com.buxbuddy.auth.repository.RoleRepository;
import com.buxbuddy.auth.repository.UserRepository;
import com.buxbuddy.auth.security.JwtService;
import com.buxbuddy.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        // 1. Check email exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        // 2. Get default role (CUSTOMER)
        Role role = roleRepository.findByName(RoleType.CUSTOMER).orElseThrow(() -> new RuntimeException("Role not found"));
        // 3. Create user
        User user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName()).email(request.getEmail()).phone(request.getPhone()).password(passwordEncoder.encode(request.getPassword())).roles(Set.of(role)).enabled(true).build();
        // 4. Save user
        User savedUser = userRepository.save(user);
        // 5. Return response
        return RegisterResponse.builder().userId(savedUser.getId()).email(savedUser.getEmail()).message("User registered successfully").build();
    }
    @Override
    public RegisterResponse login(RegisterRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Get logged-in user details
            User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
            // Generate token with roles + businessId
            String token = jwtService.generateToken(user);
            return RegisterResponse.builder()
                    .userId((user.getId()))
                    .email(user.getEmail())
                    .token(token)
                    .message("Login successful")
                    .role(
                            user.getRoles()
                                    .stream()
                                    .map(role -> role.getName().name())
                                    .toList()
                    )
                    .build();
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
    }

    // 🔹 GET ALL USERS
    @Override
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    // 🔹 UPDATE CURRENT USER
    @Override
    public UserResponse updateUser(UpdateUserRequest request) {

        User user = getLoggedInUser();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());

        userRepository.save(user);

        return mapToResponse(user);
    }

    // 🔹 DELETE CURRENT USER
    @Override
    public void deleteUser() {

        User user = getLoggedInUser();
        userRepository.delete(user);
    }

    // 🔐 GET LOGGED IN USER
    private User getLoggedInUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // 🔄 MAP ENTITY → DTO
    private UserResponse mapToResponse(User user) {

        return UserResponse.builder().id(user.getId()).firstName(user.getFirstName()).lastName(user.getLastName()).email(user.getEmail()).phone(user.getPhone()).enabled(user.getEnabled()).roles(user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet())).build();
    }
}
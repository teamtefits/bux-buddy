package com.buxbuddy.auth.controller;

import com.buxbuddy.auth.dto.business.BusinessResponse;
import com.buxbuddy.auth.dto.user.UserResponse;
import com.buxbuddy.auth.dto.user.UserUpdateRequest;
import com.buxbuddy.auth.service.AdminService;
import com.buxbuddy.auth.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class AdminController {
    private final AdminService adminService;
    private final BusinessService businessService;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {

        return ResponseEntity.ok(adminService.getUserById(id));
    }
    @GetMapping("/users/email")
    public ResponseEntity<UserResponse> getUserByEmail(
            @RequestParam String email) {
        return ResponseEntity.ok(adminService.getUserByEmail(email));
    }
    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable String id, @RequestBody UserUpdateRequest request) {

        return ResponseEntity.ok(adminService.updateUser(id, request));
    }
    @PatchMapping("/users/{id}/status")
    public ResponseEntity<String> updateUserStatus(@PathVariable String id, @RequestParam Boolean enabled) {
        adminService.updateUserStatus(id, enabled);
        return ResponseEntity.ok("User status updated");
    }
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
    @GetMapping
    public ResponseEntity<List<BusinessResponse>> getAll() {
        return ResponseEntity.ok(businessService.getAllBusinesses());
    }
}
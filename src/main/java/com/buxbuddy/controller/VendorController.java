package com.buxbuddy.controller;

import com.buxbuddy.dto.ApiResponse;
import com.buxbuddy.dto.VendorDTO;
import com.buxbuddy.entity.Vendor;
import com.buxbuddy.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
@CrossOrigin(origins = "*")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @PostMapping
    public ResponseEntity<ApiResponse<Vendor>> saveVendor(@RequestBody VendorDTO dto) {
        Vendor vendor = vendorService.saveVendor(dto);

        return ResponseEntity.ok(
                new ApiResponse<>("Vendor created successfully", vendor)
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Vendor>>> getAllVendors() {
        List<Vendor> vendors = vendorService.getAllVendors();

        return ResponseEntity.ok(
                new ApiResponse<>("Vendors fetched successfully", vendors)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Vendor>> getVendor(@PathVariable Long id) {
        Vendor vendor = vendorService.getVendorById(id);

        return ResponseEntity.ok(
                new ApiResponse<>("Vendor fetched successfully", vendor)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Vendor>> updateVendor(
            @PathVariable Long id,
            @RequestBody VendorDTO dto) {

        Vendor updated = vendorService.updateVendor(id, dto);

        return ResponseEntity.ok(
                new ApiResponse<>("Vendor updated successfully", updated)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteVendor(@PathVariable Long id) {

        vendorService.deleteVendor(id);

        return ResponseEntity.ok(
                new ApiResponse<>("Vendor deleted successfully", "SUCCESS")
        );
    }
}
package com.buxbuddy.service;

import com.buxbuddy.dto.VendorDTO;
import com.buxbuddy.entity.Vendor;
import com.buxbuddy.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    public Vendor saveVendor(VendorDTO dto) {

        if (vendorRepository.existsByVendorsname(dto.getVendorName())) {
            throw new RuntimeException("Vendor already exists.");
        }

        Vendor vendor = new Vendor();
        vendor.setVendorsname(dto.getVendorName());
        vendor.setContactPerson(dto.getContactPerson());
        vendor.setPhone(dto.getPhone());
        vendor.setEmail(dto.getEmail());
        vendor.setAddress(dto.getAddress());

        return vendorRepository.save(vendor);
    }

    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    public Vendor getVendorById(Long id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
    }

    public Vendor updateVendor(Long id, VendorDTO dto) {

        Vendor vendor = getVendorById(id);

        vendor.setVendorsname(dto.getVendorName());
        vendor.setContactPerson(dto.getContactPerson());
        vendor.setPhone(dto.getPhone());
        vendor.setEmail(dto.getEmail());
        vendor.setAddress(dto.getAddress());

        return vendorRepository.save(vendor);
    }

    public void deleteVendor(Long id) {
        vendorRepository.deleteById(id);
    }
}

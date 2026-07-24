package com.buxbuddy.auth.dto.vendor;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class VendorResponse {
    private Long vendorId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Long businessId;
    private String businessName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
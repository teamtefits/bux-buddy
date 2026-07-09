package com.buxbuddy.auth.dto.vendor;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class VendorResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String status;
    private Long businessId;
}
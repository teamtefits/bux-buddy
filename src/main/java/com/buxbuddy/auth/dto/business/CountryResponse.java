package com.buxbuddy.auth.dto.business;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CountryResponse {
    private Long id;
    private String countryName;
    private String countryCode;
}

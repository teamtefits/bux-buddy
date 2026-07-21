package com.buxbuddy.auth.dto.business;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProvinceResponse {
    private Long id;
    private String provinceName;
    private String provinceCode;
    private CountryResponse country;
}
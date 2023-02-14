package com.nicolebertolo.mscustomer.domain.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Address {
    private String addressName;
    private String countryName;
    private String zipCode;
    private Boolean isPrincipal;
}

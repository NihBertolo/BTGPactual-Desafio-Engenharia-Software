package com.nicolebertolo.mscustomer.domain.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDocument {
    private String documentType;
    private String documentNumber;
}

package com.nicolebertolo.msbackendforfronted.grpc.client.domain.customer;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CustomerResponse {
    private String id;
    private String name;
    private String lastname;
    private List<CustomerDocument> documents;
    private List<Address> addresses;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;

    @Data
    @Builder
    public static class CustomerDocument {
        private String documentType;
        private String documentNumber;
    }

    @Data
    @Builder
    public static class Address {
        private String addressName;
        private String countryName;
        private String zipCode;
        private Boolean isPrincipal;
    }


}

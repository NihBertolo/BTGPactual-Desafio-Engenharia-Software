package com.nicolebertolo.msbackendforfronted.grpc.client.domain.customer;

import com.nicolebertolo.grpc.customerapi.CustomerDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public static CustomerResponse toResponse(CustomerDto customerDto) {
        return CustomerResponse.builder()
                .id(customerDto.getId())
                .name(customerDto.getName())
                .lastname(customerDto.getLastname())
                .documents(customerDto.getCustomerDocumentDtoList().stream().map( document ->
                        CustomerDocument.builder()
                                .documentNumber(document.getDocumentNumber())
                                .documentType(document.getDocumentType())
                                .build()
                ).collect(Collectors.toList()))
                .addresses(customerDto.getCustomerAddressDtoList().stream().map( address ->
                        Address.builder()
                                .addressName(address.getAddressName())
                                .countryName(address.getCountryName())
                                .zipCode(address.getZipCode())
                                .isPrincipal(address.getIsPrincipal())
                                .build()
                ).collect(Collectors.toList()))
                .creationDate(LocalDateTime.parse(customerDto.getCreationDate()))
                .lastUpdateDate(LocalDateTime.parse(customerDto.getLastUpdateDate()))
                .build();
    }

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

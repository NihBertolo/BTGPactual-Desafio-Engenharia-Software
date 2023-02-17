package com.nicolebertolo.msbackendforfronted.grpc.client.domain.customer;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CustomerRequest {

    private String name;
    private String lastname;
    private List<CustomerResponse.CustomerDocument> documents;
    private List<CustomerResponse.Address> addresses;
}

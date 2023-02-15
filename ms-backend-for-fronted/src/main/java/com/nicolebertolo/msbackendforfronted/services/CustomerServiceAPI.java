package com.nicolebertolo.msbackendforfronted.services;

import com.nicolebertolo.msbackendforfronted.grpc.client.domain.customer.CustomerResponse;
import com.nicolebertolo.msbackendforfronted.grpc.client.service.CustomerServiceGRPC;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.nicolebertolo.msbackendforfronted.grpc.client.domain.customer.CustomerResponse.toResponse;

@Service
public class CustomerServiceAPI {

    @Autowired
    private CustomerServiceGRPC customerServiceGRPC;
    public CustomerResponse findCustomerById(String customerId, String tracing) {
        val customerResponseGRPC = customerServiceGRPC.findCustomerById(customerId, tracing);
        return toResponse(customerResponseGRPC.getCustomerDto());
    }

}

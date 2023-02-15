package com.nicolebertolo.msbackendforfronted.services;

import com.nicolebertolo.msbackendforfronted.grpc.client.domain.customer.CustomerRequest;
import com.nicolebertolo.msbackendforfronted.grpc.client.domain.customer.CustomerResponse;
import com.nicolebertolo.msbackendforfronted.grpc.client.service.CustomerServiceGRPC;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

import static com.nicolebertolo.msbackendforfronted.grpc.client.domain.customer.CustomerResponse.toResponse;

@Service
public class CustomerServiceAPI {

    @Autowired
    private CustomerServiceGRPC customerServiceGRPC;

    public CustomerResponse findCustomerById(String customerId, String tracing) {
        val customerResponseGRPC = customerServiceGRPC.findCustomerById(customerId, tracing);
        return toResponse(customerResponseGRPC.getCustomerDto());
    }

    public CustomerResponse createCustomer(CustomerRequest customerRequest, String tracing) {
        val createCustomer = customerServiceGRPC.createCustomer(customerRequest, tracing);
        return toResponse(createCustomer.getCustomerDto());
    }

    public List<CustomerResponse> findAll(String tracing) {
        val customerResponseGRPC = customerServiceGRPC.findAllCustomers(tracing);
        return customerResponseGRPC.getCustomerDtoList().stream()
                .map(CustomerResponse::toResponse)
                .collect(Collectors.toList());
    }

}

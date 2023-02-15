package com.nicolebertolo.msbackendforfronted.controllers;

import com.nicolebertolo.msbackendforfronted.grpc.client.domain.customer.CustomerResponse;
import com.nicolebertolo.msbackendforfronted.services.CustomerServiceAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/v1/customers")
public class CustomerController {
    @Autowired
    private CustomerServiceAPI customerServiceAPI;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @GetMapping("/{id")
    public CustomerResponse findCustomerById(@PathVariable(value = "id") String customerId) {
        LOGGER.info("[CustomerController.findCustomerById] - Controller Request");
        String tracing = UUID.randomUUID().toString();
        return this.customerServiceAPI.findCustomerById(customerId, tracing);
    }

    @GetMapping
    public List<CustomerResponse> findAllCustomers() {
        LOGGER.info("[CustomerController.findAllCustomers] - Controller Request");

        return null;
    }

}

package com.nicolebertolo.msbackendforfronted.controllers;

import com.nicolebertolo.msbackendforfronted.grpc.client.domain.customer.CustomerRequest;
import com.nicolebertolo.msbackendforfronted.grpc.client.domain.customer.CustomerResponse;
import com.nicolebertolo.msbackendforfronted.services.CustomerServiceAPI;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/v1/customers")
public class CustomerController {
    @Autowired
    private CustomerServiceAPI customerServiceAPI;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> findCustomerById(@PathVariable(value = "id") String customerId) {
        LOGGER.info("[CustomerController.findCustomerById] - Controller Request");
        String tracing = UUID.randomUUID().toString();
        return ResponseEntity.status(HttpStatus.OK).body(this.customerServiceAPI.findCustomerById(customerId, tracing));
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CustomerRequest customerRequest) {
        LOGGER.info("[CustomerController.createCustomer] - Controller Request");
        String tracing = UUID.randomUUID().toString();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.customerServiceAPI.createCustomer(customerRequest, tracing));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAllCustomers() {
        LOGGER.info("[CustomerController.findAllCustomers] - Controller Request");
        val tracing = UUID.randomUUID().toString();

        return ResponseEntity.status(HttpStatus.OK).body(this.customerServiceAPI.findAll(tracing));
    }

}

package com.nicolebertolo.msbackendforfronted.grpc.client.service;


import com.nicolebertolo.grpc.customerapi.CreateCustomerResponse;
import com.nicolebertolo.grpc.customerapi.CustomerServiceAPIGrpc;
import com.nicolebertolo.grpc.customerapi.FindCustomerByIdRequest;
import com.nicolebertolo.grpc.customerapi.FindCustomerByIdResponse;
import com.nicolebertolo.msbackendforfronted.grpc.client.component.CustomerGrpcClient;
import io.grpc.ManagedChannel;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class CustomerServiceGRPC {

    @Autowired
    private CustomerGrpcClient customerGrpcClient;
    private ManagedChannel channel = this.customerGrpcClient.getChannel();

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public FindCustomerByIdResponse findCustomerById(String customerId, String tracing) {
        LOGGER.info("[CustomerServiceGrpc.findCustomerById] - Init GRPC Communication");
        val findCustomerByIdRequest = FindCustomerByIdRequest.newBuilder()
                .setId(customerId)
                .setTracing(tracing)
                .build();

        return CustomerServiceAPIGrpc.newBlockingStub(channel).findCustomerById(findCustomerByIdRequest);
    }

    public CreateCustomerResponse createCustomer(CustomerRequest customerRequest) {
        LOGGER.info("[]");
    }
}

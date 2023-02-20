package com.nicolebertolo.msbackendforfronted.grpc.client.service;


import com.nicolebertolo.grpc.customerapi.*;
import com.nicolebertolo.msbackendforfronted.exceptions.OperationException;
import com.nicolebertolo.msbackendforfronted.exceptions.ResourceNotFoundException;
import com.nicolebertolo.msbackendforfronted.grpc.client.domain.customer.CustomerRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.stream.Collectors;

@Service
public class CustomerServiceGRPC {

    @Value("${grpc.clients.customer.address}")
    private String address;

    @Value("${grpc.clients.customer.port}")
    private int port;

    private ManagedChannel getChannel() {
        return ManagedChannelBuilder.forAddress(address, port).usePlaintext().build();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public FindCustomerByIdResponse findCustomerById(String customerId, String tracing) {
        LOGGER.info("[CustomerServiceGRPC.findCustomerById] - Init GRPC Communication, tracing: " + tracing);
        try {
            val findCustomerByIdRequest = FindCustomerByIdRequest.newBuilder()
                    .setId(customerId)
                    .setTracing(tracing)
                    .build();

            return CustomerServiceAPIGrpc.newBlockingStub(this.getChannel()).findCustomerById(findCustomerByIdRequest);
        } catch (StatusRuntimeException ex) {
            if (ex.getStatus().getCode().toStatus().equals(Status.NOT_FOUND)) {
                throw new ResourceNotFoundException("Customer with id: " + customerId + " not found.");
            } else {
                throw new OperationException("Error at communication.");
            }
        }
    }

    public CreateCustomerResponse createCustomer(CustomerRequest customerRequest, String tracing) {
        LOGGER.info("[CustomerServiceGRPC.createCustomer] - Init GRPC Communication, tracing: " + tracing);

        try {
            val createCustomerRequest = CreateCustomerRequest.newBuilder()
                    .setName(customerRequest.getName())
                    .setLastname(customerRequest.getLastname())
                    .setTracing(tracing)
                    .addAllCustomerDocumentDto(customerRequest.getDocuments().stream().map(document ->
                            CustomerDocumentDto.newBuilder()
                                    .setDocumentNumber(document.getDocumentNumber())
                                    .setDocumentType(document.getDocumentType())
                                    .build()
                    ).collect(Collectors.toList()))
                    .addAllCustomerAddressDto(customerRequest.getAddresses().stream().map(address ->
                            CustomerAddressDto.newBuilder()
                                    .setAddressName(address.getAddressName())
                                    .setCountryName(address.getCountryName())
                                    .setZipCode(address.getZipCode())
                                    .setIsPrincipal(address.getIsPrincipal())
                                    .build()
                    ).collect(Collectors.toList()))
                    .build();

            return CustomerServiceAPIGrpc.newBlockingStub(this.getChannel()).createCustomer(createCustomerRequest);
        } catch (StatusRuntimeException ex) {
            throw new OperationException("Error at communication.");
        }
    }

    public FindAllCustomersResponse findAllCustomers(String tracing) {
        LOGGER.info("[CustomerServiceGRPC.findAllCustomers] - Init GRPC Communication, tracing: " + tracing);

        try {
            val findAllCustomersRequest = FindAllCustomersRequest.newBuilder()
                    .setTracing(tracing)
                    .build();

            return CustomerServiceAPIGrpc.newBlockingStub(this.getChannel()).findAllCustomers(findAllCustomersRequest);
        } catch (StatusRuntimeException ex) {
            throw new OperationException("Error at communication.");
        }
    }

}

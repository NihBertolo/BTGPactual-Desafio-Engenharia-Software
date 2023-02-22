package com.nicolebertolo.mscustomer.grpc.server;


import com.nicolebertolo.grpc.customerapi.*;
import com.nicolebertolo.mscustomer.domain.models.Address;
import com.nicolebertolo.mscustomer.domain.models.Customer;
import com.nicolebertolo.mscustomer.domain.models.CustomerDocument;
import com.nicolebertolo.mscustomer.exceptions.handlers.GrpcErrorHandler;
import com.nicolebertolo.mscustomer.grpc.server.request.CustomerRequest;
import com.nicolebertolo.mscustomer.grpc.server.response.CustomerResponse;
import com.nicolebertolo.mscustomer.services.CustomerService;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.val;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.invoke.MethodHandles;
import java.util.stream.Collectors;

import static io.grpc.netty.shaded.io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;

@GrpcService
public class CustomerGrpcService extends CustomerServiceAPIGrpc.CustomerServiceAPIImplBase {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private GrpcErrorHandler grpcErrorHandler;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void findCustomerById(
            FindCustomerByIdRequest request,
            StreamObserver<FindCustomerByIdResponse> responseObserver
    ) {
        try {
            LOGGER.info("[CustomerServer.findCustomerById] - Finding Customer by Id, tracing: " + request.getTracing());

            val customer = this.customerService.findCustomerById(request.getId());
            val customerResponse = FindCustomerByIdResponse.newBuilder().setCustomerDto(toDto(customer)).build();
            LOGGER.info("[CustomerServer.findCustomerById] - Found Customer by Id, tracing: " + request.getTracing());

            responseObserver.onNext(customerResponse);
            responseObserver.onCompleted();
        } catch (StatusRuntimeException ex) {
            responseObserver.onError(grpcErrorHandler.handle(ex));
        }
    }

    @Override
    public void createCustomer(
            CreateCustomerRequest request,
            StreamObserver<CreateCustomerResponse> responseObserver
    ) {
        try {
            LOGGER.info("[CustomerServer.createCustomer] - Creating customer, tracing: " + request.getTracing());
            val customerRequest = CustomerRequest.builder()
                    .name(request.getName())
                    .lastname(request.getLastname())
                    .documents(request.getCustomerDocumentDtoList().stream().map(document ->
                            CustomerDocument.builder()
                                    .documentNumber(document.getDocumentNumber())
                                    .documentType(document.getDocumentType())
                                    .build()
                    ).collect(Collectors.toList()))
                    .addresses(request.getCustomerAddressDtoList().stream().map(address ->
                            Address.builder()
                                    .addressName(address.getAddressName())
                                    .countryName(address.getCountryName())
                                    .zipCode(address.getZipCode())
                                    .isPrincipal(address.getIsPrincipal())
                                    .build()
                    ).collect(Collectors.toList()))
                    .build();
            val customer = this.customerService.createCustomer(customerRequest);
            val customerResponse = CreateCustomerResponse.newBuilder().setCustomerDto(toDto(customer)).build();

            LOGGER.info("[CustomerServer.createCustomer] - Customer created with Id: " + customer.getId()
                    + ", tracing: " + request.getTracing());

            responseObserver.onNext(customerResponse);
            responseObserver.onCompleted();
        } catch (StatusRuntimeException ex) {
            responseObserver.onError(grpcErrorHandler.handle(ex));
        }
    }

    @Override
    public void findAllCustomers(
            FindAllCustomersRequest request,
            StreamObserver<FindAllCustomersResponse> responseObserver
    ) {
        try {
            LOGGER.info("[CustomerServer.findAllCustomers] - Finding all customers, tracing: " + request.getTracing());

            val customers = this.customerService.findAll();
            val customersResponse = FindAllCustomersResponse.newBuilder()
                    .addAllCustomerDto(customers.stream()
                            .map(this::toDto)
                            .collect(Collectors.toList()))
                    .build();

            responseObserver.onNext(customersResponse);
            responseObserver.onCompleted();
        } catch (StatusRuntimeException ex) {
            responseObserver.onError(grpcErrorHandler.handle(ex));
        }
    }

    private CustomerDto toDto(Customer customer) {
        return CustomerDto.newBuilder()
                .setId(customer.getId())
                .setName(customer.getName())
                .setLastname(customer.getLastname())
                .addAllCustomerAddressDto(customer.getAddresses().stream().map(address ->
                        CustomerAddressDto.newBuilder()
                                .setAddressName(address.getAddressName())
                                .setCountryName(address.getCountryName())
                                .setZipCode(address.getZipCode())
                                .setIsPrincipal(address.getIsPrincipal())
                                .build()
                ).collect(Collectors.toList()))
                .addAllCustomerDocumentDto(customer.getDocuments().stream().map(document ->
                        CustomerDocumentDto.newBuilder()
                                .setDocumentNumber(document.getDocumentNumber())
                                .setDocumentType(document.getDocumentType())
                                .build()
                ).collect(Collectors.toList()))
                .setCreationDate(customer.getCreationDate().toString())
                .build();
    }
}

package com.nicolebertolo.mscustomer.grpc.client;


import com.nicolebertolo.grpc.customerapi.CustomerServiceAPIGrpc.CustomerServiceAPIImplBase;
import com.nicolebertolo.grpc.customerapi.FindCustomerByIdRequest;
import com.nicolebertolo.grpc.customerapi.FindCustomerByIdResponse;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class CustomerServer extends CustomerServiceAPIImplBase {

    @Override
    public void findCustomerById(
            FindCustomerByIdRequest request,
            StreamObserver<FindCustomerByIdResponse> responseObserver
    ) {
        try {

        } catch (StatusRuntimeException ex) {

        }
    }
}

package com.nicolebertolo.mspayment.application.ports.input.grpc;

import com.nicolebertolo.grpc.customerapi.*;
import io.grpc.stub.StreamObserver;

public interface PaymentServerUseCase {

    void findPaymentById(
            FindPaymentByIdRequest request,
            StreamObserver<FindPaymentByIdResponse> responseObserver
    );

    void postPaymentById(
            PostPaymentByIdRequest request,
            StreamObserver<PostPaymentByIdResponse> responseObserver
    );

    void findAllPayments(
            FindAllPaymentsRequest request,
            StreamObserver<FindAllPaymentsResponse> responseObserver
    );
}

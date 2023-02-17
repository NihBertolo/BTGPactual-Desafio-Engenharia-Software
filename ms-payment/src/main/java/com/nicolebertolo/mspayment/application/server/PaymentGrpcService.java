package com.nicolebertolo.mspayment.application.server;

import com.nicolebertolo.grpc.customerapi.*;
import com.nicolebertolo.grpc.customerapi.PaymentServiceAPIGrpc.PaymentServiceAPIImplBase;
import com.nicolebertolo.mspayment.application.domain.enums.PaymentStatus;
import com.nicolebertolo.mspayment.application.ports.input.grpc.PaymentServerUseCase;
import com.nicolebertolo.mspayment.application.service.PaymentService;
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
public class PaymentGrpcService extends PaymentServiceAPIImplBase implements PaymentServerUseCase {

    @Autowired
    private PaymentService paymentService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void findPaymentById(
            FindPaymentByIdRequest request,
            StreamObserver<FindPaymentByIdResponse> responseObserver
    ) {
        try {
            LOGGER.info("[PaymentServer.findPaymentById] - Payment found by Id, tracing: " + request.getTracing());

            val payment = this.paymentService.findPaymentById(request.getPaymentId());

            val paymentResponse = FindPaymentByIdResponse.newBuilder()
                    .setPaymentDto(
                            PaymentDto.newBuilder()
                                    .setId(payment.getId())
                                    .setOrderId(payment.getOrderId())
                                    .setAmount(payment.getAmount().doubleValue())
                                    .setAdditionalInfo(payment.getAdditionalInfo())
                                    .setMethod(payment.getMethod().toString())
                                    .setStatus(payment.getStatus().toString())
                                    .setCreationDate(payment.getCreationDate().toString())
                                    .build()
                    ).build();

            responseObserver.onNext(paymentResponse);
            responseObserver.onCompleted();
        } catch (StatusRuntimeException ex) {
            if (ex.getStatus().equals(BAD_REQUEST)) {
                val status = Status.INVALID_ARGUMENT.withDescription(ex.getMessage());
                responseObserver.onError(status.asRuntimeException());
            }
        }
    }

    @Override
    public void postPaymentById(
            PostPaymentByIdRequest request,
            StreamObserver<PostPaymentByIdResponse> responseObserver
    ) {
        try {
            LOGGER.info("[PaymentServer.postPaymentById] - Posting payment by Id, tracing: " + request.getTracing());

            val payment = this.paymentService.handlePaymentStatusById(
                    PaymentStatus.valueOf(request.getStatus()),
                    request.getPaymentId(),
                    request.getTracing()
            );

            val paymentResponse = PostPaymentByIdResponse.newBuilder()
                    .setPaymentDto(
                            PaymentDto.newBuilder()
                                    .setId(payment.getId())
                                    .setOrderId(payment.getOrderId())
                                    .setAmount(payment.getAmount().doubleValue())
                                    .setAdditionalInfo(payment.getAdditionalInfo())
                                    .setMethod(payment.getMethod().toString())
                                    .setStatus(payment.getStatus().toString())
                                    .setCreationDate(payment.getCreationDate().toString())
                                    .build()
                    ).build();

            responseObserver.onNext(paymentResponse);
            responseObserver.onCompleted();
        } catch (StatusRuntimeException ex) {
            if (ex.getStatus().equals(BAD_REQUEST)) {
                val status = Status.INVALID_ARGUMENT.withDescription(ex.getMessage());
                responseObserver.onError(status.asRuntimeException());
            }
        }
    }

    @Override
    public void findAllPayments(
            FindAllPaymentsRequest request,
            StreamObserver<FindAllPaymentsResponse> responseObserver
    ) {
        try {
            LOGGER.info("[PaymentServer.findAllPayments] - Finding all payments, tracing: " + request.getTracing());

            val payments = this.paymentService.findAll();

            val paymentsResponse = FindAllPaymentsResponse.newBuilder()
                    .addAllPaymentDto(
                            payments.stream().map(payment ->
                                    PaymentDto.newBuilder()
                                            .setId(payment.getId())
                                            .setOrderId(payment.getOrderId())
                                            .setAmount(payment.getAmount().doubleValue())
                                            .setAdditionalInfo(payment.getAdditionalInfo())
                                            .setMethod(payment.getMethod().toString())
                                            .setStatus(payment.getStatus().toString())
                                            .setCreationDate(payment.getCreationDate().toString())
                                            .build()
                            ).collect(Collectors.toList())
                    ).build();

            responseObserver.onNext(paymentsResponse);
            responseObserver.onCompleted();
        } catch (StatusRuntimeException ex) {
            if (ex.getStatus().equals(BAD_REQUEST)) {
                val status = Status.INVALID_ARGUMENT.withDescription(ex.getMessage());
                responseObserver.onError(status.asRuntimeException());
            }
        }
    }
}

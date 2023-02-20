package com.nicolebertolo.mspayment.application.server;

import com.nicolebertolo.grpc.customerapi.*;
import com.nicolebertolo.grpc.customerapi.PaymentServiceAPIGrpc.PaymentServiceAPIImplBase;
import com.nicolebertolo.mspayment.application.domain.enums.PaymentMethod;
import com.nicolebertolo.mspayment.application.domain.enums.PaymentStatus;
import com.nicolebertolo.mspayment.application.domain.models.Payment;
import com.nicolebertolo.mspayment.application.excpetions.CantConvertEnumException;
import com.nicolebertolo.mspayment.application.excpetions.handlers.GrpcErrorHandler;
import com.nicolebertolo.mspayment.application.ports.input.grpc.PaymentServerUseCase;
import com.nicolebertolo.mspayment.application.service.PaymentService;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.val;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.invoke.MethodHandles;
import java.util.stream.Collectors;

@GrpcService
public class PaymentGrpcService extends PaymentServiceAPIImplBase implements PaymentServerUseCase {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private GrpcErrorHandler grpcErrorHandler;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void findPaymentById(
            FindPaymentByIdRequest request,
            StreamObserver<FindPaymentByIdResponse> responseObserver
    ) {
        try {
            LOGGER.info("[PaymentServer.findPaymentById] - Payment found by Id, tracing: " + request.getTracing());

            val payment = this.paymentService.findPaymentById(request.getPaymentId());

            val paymentResponse = FindPaymentByIdResponse.newBuilder().setPaymentDto(toDto(payment)).build();

            responseObserver.onNext(paymentResponse);
            responseObserver.onCompleted();
        } catch (StatusRuntimeException ex) {
            responseObserver.onError(grpcErrorHandler.handle(ex));
        }
    }

    @Override
    public void postPaymentById(
            PostPaymentByIdRequest request,
            StreamObserver<PostPaymentByIdResponse> responseObserver
    ) {
        try {
            LOGGER.info("[PaymentServer.postPaymentById] - Posting payment by Id, tracing: " + request.getTracing());
            PaymentStatus paymentStatus;
            PaymentMethod paymentMethod;

            try {
                paymentStatus = PaymentStatus.valueOf(request.getStatus());
                paymentMethod = PaymentMethod.valueOf(request.getMethod());
            } catch (Exception ex) {
                throw new CantConvertEnumException("Can't convert enum, with cause: " +ex.getMessage());
            }

            val payment = this.paymentService.handlePaymentStatusById(
                    paymentStatus,
                    paymentMethod,
                    request.getPaymentId(),
                    request.getTracing()
            );

            val paymentResponse = PostPaymentByIdResponse.newBuilder().setPaymentDto(toDto(payment)).build();

            responseObserver.onNext(paymentResponse);
            responseObserver.onCompleted();
        } catch (StatusRuntimeException ex) {
            responseObserver.onError(grpcErrorHandler.handle(ex));
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
                    .addAllPaymentDto(payments.stream().map(this::toDto).collect(Collectors.toList())).build();

            responseObserver.onNext(paymentsResponse);
            responseObserver.onCompleted();
        } catch (StatusRuntimeException ex) {
            responseObserver.onError(grpcErrorHandler.handle(ex));
        }
    }

    private PaymentDto toDto(Payment payment) {
        return PaymentDto.newBuilder()
                .setId(payment.getId())
                .setOrderId(payment.getOrderId())
                .setAmount(payment.getAmount().doubleValue())
                .setAdditionalInfo(payment.getAdditionalInfo())
                .setMethod((payment.getMethod() != null) ? payment.getMethod().toString() : "")
                .setStatus(payment.getStatus().toString())
                .setCreationDate(payment.getCreationDate().toString())
                .build();
    }
}

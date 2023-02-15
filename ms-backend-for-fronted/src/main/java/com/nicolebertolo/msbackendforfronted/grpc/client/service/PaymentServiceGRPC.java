package com.nicolebertolo.msbackendforfronted.grpc.client.service;

import com.nicolebertolo.grpc.customerapi.*;
import com.nicolebertolo.msbackendforfronted.grpc.client.component.PaymentGrpcClient;
import com.nicolebertolo.msbackendforfronted.grpc.client.domain.payment.PaymentRequest;
import com.nicolebertolo.msbackendforfronted.grpc.client.domain.payment.PaymentResponse;
import io.grpc.ManagedChannel;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

import static com.nicolebertolo.msbackendforfronted.grpc.client.domain.payment.PaymentResponse.toResponse;

@Service
public class PaymentServiceGRPC extends PaymentServiceAPIGrpc.PaymentServiceAPIImplBase {

    @Autowired
    private PaymentGrpcClient paymentGrpcClient;

    private ManagedChannel channel = this.paymentGrpcClient.getChannel();
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public PaymentResponse findPaymentById(String paymentId, String tracing) {
        LOGGER.info("[PaymentServiceGRPC.findPaymentById] - Init GRPC Communication");
        val findPaymentByIdRequest = FindPaymentByIdRequest.newBuilder()
                .setPaymentId(paymentId)
                .setTracing(tracing)
                .build();

        return toResponse(PaymentServiceAPIGrpc.newBlockingStub(channel)
                .findPaymentById(findPaymentByIdRequest)
                .getPaymentDto()
        );
    }

    public PaymentResponse postPayment(PaymentRequest paymentRequest, String tracing) {
        LOGGER.info("[PaymentServiceGRPC.postPayment] - Init GRPC Communication");
        val postPaymentRequest = PostPaymentByIdRequest.newBuilder()
                .setPaymentId(paymentRequest.getId())
                .setMethod(paymentRequest.getMethod())
                .setStatus(paymentRequest.getStatus())
                .setTracing(tracing)
                .build();

        return toResponse(PaymentServiceAPIGrpc.newBlockingStub(channel)
                .postPaymentById(postPaymentRequest).getPaymentDto());
    }

    public List<PaymentResponse> findAllPayments(String tracing) {
        LOGGER.info("[PaymentServiceGRPC.findAllPayments] - Init GRPC Communication");
        val findAllPaymentsRequest = FindAllPaymentsRequest.newBuilder()
                .setTracing(tracing)
                .build();

        return PaymentServiceAPIGrpc.newBlockingStub(channel)
                .findAllPayments(findAllPaymentsRequest)
                .getPaymentDtoList().stream().map(PaymentResponse::toResponse).collect(Collectors.toList());
    }
}

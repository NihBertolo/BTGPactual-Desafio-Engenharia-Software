package com.nicolebertolo.msbackendforfronted.services;

import com.nicolebertolo.msbackendforfronted.exceptions.ResourceNotFoundException;
import com.nicolebertolo.msbackendforfronted.grpc.client.domain.payment.PaymentRequest;
import com.nicolebertolo.msbackendforfronted.grpc.client.domain.payment.PaymentResponse;
import com.nicolebertolo.msbackendforfronted.grpc.client.service.PaymentServiceGRPC;
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
public class PaymentServiceAPI {

    @Autowired
    private PaymentServiceGRPC paymentServiceGRPC;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public PaymentResponse findPaymentById(String paymentId, String tracing) throws ResourceNotFoundException {
        val paymentGRPC = this.paymentServiceGRPC.findPaymentById(paymentId, tracing);
        LOGGER.info("[PaymentServiceAPI.findPaymentById] - Converting gRPC protobuf to Model, tracing: " +tracing);

        return toResponse(paymentGRPC.getPaymentDto());
    }

    public PaymentResponse postPayment(PaymentRequest paymentRequest, String tracing) {
        val paymentGRPC = this.paymentServiceGRPC.postPayment(paymentRequest, tracing);
        LOGGER.info("[PaymentServiceAPI.postPayment] - Converting gRPC protobuf to Model, tracing: " +tracing);

        return toResponse(paymentGRPC.getPaymentDto());
    }

    public List<PaymentResponse> findAllPayments(String tracing) {
        val paymentGRPC = this.paymentServiceGRPC.findAllPayments(tracing);
        LOGGER.info("[PaymentServiceAPI.findAllPayments] - Converting gRPC protobuf to Model, tracing: " +tracing);

        return paymentGRPC.getPaymentDtoList().stream().map(PaymentResponse::toResponse).collect(Collectors.toList());
    }
}

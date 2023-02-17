package com.nicolebertolo.msbackendforfronted.controllers;

import com.nicolebertolo.msbackendforfronted.grpc.client.domain.payment.PaymentRequest;
import com.nicolebertolo.msbackendforfronted.grpc.client.domain.payment.PaymentResponse;
import com.nicolebertolo.msbackendforfronted.grpc.client.service.PaymentServiceGRPC;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/payments")
public class PaymentController {

    @Autowired
    private PaymentServiceGRPC paymentServiceGRPC;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @GetMapping("/{id}")
    public PaymentResponse findPaymentById(@PathVariable("id") String paymentId) {
        LOGGER.info("[PaymentController.findPaymentById] - Controller Request");
        val tracing = UUID.randomUUID().toString();

        return this.paymentServiceGRPC.findPaymentById(paymentId, tracing);
    }

    @PostMapping
    public PaymentResponse postPayment(@RequestBody PaymentRequest paymentRequest) {
        LOGGER.info("[PaymentController.postPayment] - Controller Request");
        val tracing = UUID.randomUUID().toString();

        return this.paymentServiceGRPC.postPayment(paymentRequest, tracing);
    }

    @GetMapping
    public List<PaymentResponse> findAllPayments() {
        LOGGER.info("[PaymentController.findAllPayments] - Controller Request");
        val tracing = UUID.randomUUID().toString();

        return this.paymentServiceGRPC.findAllPayments(tracing);
    }
}

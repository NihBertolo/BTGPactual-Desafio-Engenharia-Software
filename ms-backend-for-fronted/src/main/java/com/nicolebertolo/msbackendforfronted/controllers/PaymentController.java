package com.nicolebertolo.msbackendforfronted.controllers;

import com.nicolebertolo.msbackendforfronted.exceptions.ResourceNotFoundException;
import com.nicolebertolo.msbackendforfronted.grpc.client.domain.payment.PaymentRequest;
import com.nicolebertolo.msbackendforfronted.grpc.client.domain.payment.PaymentResponse;
import com.nicolebertolo.msbackendforfronted.services.PaymentServiceAPI;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/payments")
public class PaymentController {

    @Autowired
    private PaymentServiceAPI paymentServiceAPI;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> findPaymentById(@PathVariable("id") String paymentId) {
        LOGGER.info("[PaymentController.findPaymentById] - Controller Request");
        val tracing = UUID.randomUUID().toString();

        return ResponseEntity.status(HttpStatus.OK).body(this.paymentServiceAPI.findPaymentById(paymentId, tracing));
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> postPayment(@RequestBody PaymentRequest paymentRequest) {
        LOGGER.info("[PaymentController.postPayment] - Controller Request");
        val tracing = UUID.randomUUID().toString();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.paymentServiceAPI.postPayment(paymentRequest, tracing));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> findAllPayments() {
        LOGGER.info("[PaymentController.findAllPayments] - Controller Request");
        val tracing = UUID.randomUUID().toString();

        return ResponseEntity.status(HttpStatus.OK).body(this.paymentServiceAPI.findAllPayments(tracing));
    }
}

package com.nicolebertolo.mspayment.application.service;

import com.nicolebertolo.mspayment.application.domain.enums.PaymentStatus;
import com.nicolebertolo.mspayment.application.domain.models.Payment;
import com.nicolebertolo.mspayment.application.excpetions.PaymentNotFoundException;
import com.nicolebertolo.mspayment.application.repository.PaymentRepository;
import com.nicolebertolo.mspayment.infrastructure.adapters.request.PaymentRequest;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public Payment findPaymentById(String paymentId) {
        LOGGER.info("[PaymentService.findPaymentById] - Finding payment by id: " + paymentId);

        val payment = this.paymentRepository.findById(paymentId);

        return payment.orElse(null);
    }

    public Payment handlePaymentStatusById(PaymentStatus paymentStatus, String paymentId) {
        LOGGER.info("[PaymentService.handlePaymentStatusById] - Handling payment status: " + paymentStatus + "by id: "
                + paymentId);

        val payment = this.paymentRepository.findById(paymentId).orElseThrow(
                () -> new PaymentNotFoundException("Payment Not Found.")
        );

        payment.setStatus(paymentStatus);
        LOGGER.info("[PaymentService.handlePaymentStatusById] - PaymentStatus updated.");
        return this.paymentRepository.save(payment);
    }

    public Payment createPayment(PaymentRequest paymentRequest) {
        LOGGER.info("[PaymentService.createPayment] - Creating a new payment");

        val payment = this.paymentRepository.insert(
                Payment.builder()
                        .id(UUID.randomUUID().toString())
                        .orderId(paymentRequest.getOrderId())
                        .amount(paymentRequest.getAmount())
                        .status(PaymentStatus.PENDING)
                        .method(null)
                        .additionalInfo(paymentRequest.getAdditionalInfo())
                        .build()
        );

        LOGGER.info("[PaymentService.createPayment] - Payment entry registered with id: " +payment.getId());
        return payment;
    }

    public List<Payment> findAll() {
        LOGGER.info("[PaymentService.findAll] - Finding all payments");

        return this.paymentRepository.findAll();
    }
}

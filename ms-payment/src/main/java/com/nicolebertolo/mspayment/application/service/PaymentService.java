package com.nicolebertolo.mspayment.application.service;

import com.nicolebertolo.mspayment.application.domain.enums.PaymentMethod;
import com.nicolebertolo.mspayment.application.domain.enums.PaymentStatus;
import com.nicolebertolo.mspayment.application.domain.models.Payment;
import com.nicolebertolo.mspayment.application.excpetions.PaymentNotFoundException;
import com.nicolebertolo.mspayment.application.repository.PaymentRepository;
import com.nicolebertolo.mspayment.infrastructure.adapters.MessageTemplate;
import com.nicolebertolo.mspayment.infrastructure.adapters.output.producer.OrderReceivedPaymentProducer;
import com.nicolebertolo.mspayment.infrastructure.adapters.request.PaymentReceivedMessage;
import com.nicolebertolo.mspayment.infrastructure.adapters.request.PaymentRequest;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderReceivedPaymentProducer orderReceivedPaymentProducer;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public Payment findPaymentById(String paymentId) {
        LOGGER.info("[PaymentService.findPaymentById] - Finding payment by id: " + paymentId);

        val payment = this.paymentRepository.findById(paymentId);

        return payment.orElse(null);
    }

    public Payment handlePaymentStatusById(PaymentStatus paymentStatus, String paymentId, String tracing) {
        LOGGER.info("[PaymentService.handlePaymentStatusById] - Handling payment status: " + paymentStatus + "by id: "
                + paymentId);

        val payment = this.paymentRepository.findById(paymentId).orElseThrow(
                () -> new PaymentNotFoundException("Payment Not Found.")
        );

        payment.setStatus(paymentStatus);
        LOGGER.info("[PaymentService.handlePaymentStatusById] - PaymentStatus updated.");
        val updatedPayment = this.paymentRepository.save(payment);

        MessageTemplate<PaymentReceivedMessage> message = new MessageTemplate<>(
                "ms-order",
                tracing,
                LocalDateTime.now(),
                PaymentReceivedMessage.builder()
                        .orderId(updatedPayment.getOrderId())
                        .payedValue(updatedPayment.getAmount())
                        .paymentStatus(paymentStatus)
                        .paymentMethod(PaymentMethod.PIX)
                        .build());

        this.orderReceivedPaymentProducer.produceMessage(message);
        return updatedPayment;
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

package com.nicolebertolo.mspayment.application.service;

import com.nicolebertolo.mspayment.application.ports.input.consumer.PaymentCreatedServiceUseCase;
import com.nicolebertolo.mspayment.infrastructure.adapters.MessageTemplate;
import com.nicolebertolo.mspayment.infrastructure.adapters.request.PaymentCreatedMessage;
import com.nicolebertolo.mspayment.infrastructure.adapters.request.PaymentRequest;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class PaymentCreatedService implements PaymentCreatedServiceUseCase {

    @Autowired
    private PaymentService paymentService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void createPayment(MessageTemplate<PaymentCreatedMessage> payload) {
        LOGGER.info("[PaymentCreatedService.createPayment] - Receiving message and will create payment.");

        val payment = this.paymentService.createPayment(
                PaymentRequest.builder()
                        .orderId(payload.getMessage().getOrderId())
                        .amount(payload.getMessage().getPaymentValue())
                        .additionalInfo(payload.getMessage().getAdditionalInfo())
                        .build()
        );
        LOGGER.info("[PaymentCreatedService.createPayment] - Payment created with id:" +payment.getId());
    }
}

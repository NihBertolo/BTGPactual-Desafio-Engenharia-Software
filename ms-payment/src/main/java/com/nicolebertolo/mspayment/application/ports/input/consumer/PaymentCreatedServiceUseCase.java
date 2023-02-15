package com.nicolebertolo.mspayment.application.ports.input.consumer;

import com.nicolebertolo.mspayment.infrastructure.adapters.MessageTemplate;
import com.nicolebertolo.mspayment.infrastructure.adapters.request.PaymentCreatedMessage;

public interface PaymentCreatedServiceUseCase {

    void createPayment(MessageTemplate<PaymentCreatedMessage> payload);
}

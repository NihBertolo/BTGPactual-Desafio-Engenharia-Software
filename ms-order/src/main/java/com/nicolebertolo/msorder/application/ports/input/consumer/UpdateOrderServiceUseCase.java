package com.nicolebertolo.msorder.application.ports.input.consumer;

import com.nicolebertolo.msorder.infrastructure.adapters.MessageTemplate;
import com.nicolebertolo.msorder.infrastructure.adapters.request.PaymentReceivedMessage;

public interface UpdateOrderServiceUseCase {

    void updateOrder(MessageTemplate<PaymentReceivedMessage> payload);

}

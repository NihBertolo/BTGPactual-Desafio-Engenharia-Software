package com.nicolebertolo.msorder.infrastructure.adapters.input.consumer;

import com.nicolebertolo.msorder.application.ports.input.consumer.UpdateOrderServiceUseCase;
import com.nicolebertolo.msorder.infrastructure.adapters.MessageTemplate;
import com.nicolebertolo.msorder.infrastructure.adapters.request.PaymentReceivedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class OrderReceivedPaymentConsumer {
    @Autowired
    private UpdateOrderServiceUseCase updateOrderServiceUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Value("${rabbitmq.queues.order-received-payment-queue}")
    private static final String RECEIVED_QUEUE = "";

    @RabbitListener(queues = RECEIVED_QUEUE)
    public void consumeMessage(MessageTemplate<PaymentReceivedMessage> payload) {
        LOGGER.info("[OrderReceivedConsumer.consumeMessage] - Consuming message from queue. - Start");
        this.updateOrderServiceUseCase.updateOrder(payload);
        LOGGER.info("[OrderReceivedConsumer.consumeMessage] - Consuming message from queue. - End");
    }
}

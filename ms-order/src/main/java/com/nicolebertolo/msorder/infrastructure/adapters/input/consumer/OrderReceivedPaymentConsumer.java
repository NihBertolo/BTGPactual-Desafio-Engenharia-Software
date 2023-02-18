package com.nicolebertolo.msorder.infrastructure.adapters.input.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicolebertolo.msorder.application.ports.input.consumer.UpdateOrderServiceUseCase;
import com.nicolebertolo.msorder.infrastructure.adapters.MessageTemplate;
import com.nicolebertolo.msorder.infrastructure.adapters.request.PaymentReceivedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Type;

@Component
public class OrderReceivedPaymentConsumer {
    @Autowired
    private UpdateOrderServiceUseCase updateOrderServiceUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String RECEIVED_QUEUE = "order_received_payment_queue";

    public void consumeMessage(byte[] message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        MessageTemplate<PaymentReceivedMessage> payload =  objectMapper.readValue(message, new TypeReference<>() {});
        LOGGER.info("[OrderReceivedConsumer.consumeMessage] - Consuming message from queue. - Start - Payload: " +payload);
        this.updateOrderServiceUseCase.updateOrder(payload);
        LOGGER.info("[OrderReceivedConsumer.consumeMessage] - Consuming message from queue. - End");
    }
}

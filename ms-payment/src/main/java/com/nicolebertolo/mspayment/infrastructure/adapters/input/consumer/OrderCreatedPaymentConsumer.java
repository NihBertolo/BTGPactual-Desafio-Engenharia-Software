package com.nicolebertolo.mspayment.infrastructure.adapters.input.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicolebertolo.mspayment.application.ports.input.consumer.PaymentCreatedServiceUseCase;
import com.nicolebertolo.mspayment.configuration.RabbitMQProperties;
import com.nicolebertolo.mspayment.infrastructure.adapters.MessageTemplate;
import com.nicolebertolo.mspayment.infrastructure.adapters.request.PaymentCreatedMessage;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;

@Component
public class OrderCreatedPaymentConsumer {

    @Autowired
    private PaymentCreatedServiceUseCase paymentCreatedServiceUseCase;

    @Autowired
    private RabbitMQProperties rabbitMQProperties;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final static String CREATED_QUEUE = "order_created_payment_queue";

    public void consumeMessage(byte[] message) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        val payload = mapper.readValue(message, new TypeReference<MessageTemplate<PaymentCreatedMessage>>() {});
        LOGGER.info("[OrderReceivedConsumer.consumeMessage] - Consuming message from queue. origin:"
                + payload.getOrigin() + ", tracing: " + payload.getTracing() + " - Start - Message: " +payload);
        this.paymentCreatedServiceUseCase.createPayment(payload);
        LOGGER.info("[OrderReceivedConsumer.consumeMessage] - Consuming message from queue. - End");
    }
}

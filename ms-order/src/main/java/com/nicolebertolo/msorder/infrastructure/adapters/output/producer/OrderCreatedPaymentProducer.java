package com.nicolebertolo.msorder.infrastructure.adapters.output.producer;

import com.nicolebertolo.msorder.infrastructure.adapters.MessageTemplate;
import com.nicolebertolo.msorder.infrastructure.adapters.request.PaymentCreatedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.lang.invoke.MethodHandles;

public class OrderCreatedPaymentProducer {

    @Autowired
    private RabbitTemplate template;

    @Value("${rabbitmq.routingKey}")
    private static final String ROUTING_KEY = "";

    @Value("${rabbitmq.topics.order-created-payment-exchange}")
    private static final String CREATED_TOPIC = "";

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public void produceMessage(MessageTemplate<PaymentCreatedMessage> payload) {
        LOGGER.info("[OrderCreatedPaymentProducer.produceMessage] - Sending message, tracing:" +payload.getTracing());

        this.template.convertAndSend(CREATED_TOPIC, ROUTING_KEY, payload);
        LOGGER.info("[OrderCreatedPaymentProducer.produceMessage] - Send, tracing:" +payload.getTracing());

    }
}

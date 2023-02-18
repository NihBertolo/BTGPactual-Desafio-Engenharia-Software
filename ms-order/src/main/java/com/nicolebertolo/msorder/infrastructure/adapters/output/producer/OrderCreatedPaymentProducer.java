package com.nicolebertolo.msorder.infrastructure.adapters.output.producer;

import com.nicolebertolo.msorder.configuration.RabbitMQProperties;
import com.nicolebertolo.msorder.infrastructure.adapters.MessageTemplate;
import com.nicolebertolo.msorder.infrastructure.adapters.request.PaymentCreatedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Component
public class OrderCreatedPaymentProducer {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private RabbitMQProperties rabbitMQProperties;

    private String topic = "spring-boot-exchange";

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public void produceMessage(MessageTemplate<PaymentCreatedMessage> payload) {
        LOGGER.info("[OrderCreatedPaymentProducer.produceMessage] - Sending message, tracing:" +payload.getTracing());

        this.template.convertAndSend(topic, rabbitMQProperties.getROUTING_KEY(), payload);
        LOGGER.info("[OrderCreatedPaymentProducer.produceMessage] - Send, tracing:" +payload.getTracing());

    }
}

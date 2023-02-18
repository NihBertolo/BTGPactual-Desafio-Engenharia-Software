package com.nicolebertolo.mspayment.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class RabbitMQProperties {

    @Value("${rabbitmq.queues.order-created-payment-queue}")
    private String CREATED_QUEUE;

    @Value("${rabbitmq.queues.order-received-payment-queue}")
    private String RECEIVED_QUEUE;

    @Value("${rabbitmq.routingKey}")
    private String ROUTING_KEY;
}

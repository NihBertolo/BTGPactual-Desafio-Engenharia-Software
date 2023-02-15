package com.nicolebertolo.mspayment.infrastructure.adapters.input.consumer;

import com.nicolebertolo.mspayment.application.ports.input.consumer.PaymentCreatedServiceUseCase;
import com.nicolebertolo.mspayment.infrastructure.adapters.MessageTemplate;
import com.nicolebertolo.mspayment.infrastructure.adapters.request.PaymentCreatedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class OrderCreatedPaymentConsumer {

    @Autowired
    private PaymentCreatedServiceUseCase paymentCreatedServiceUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Value("${rabbitmq.queues.order-created-payment-queue}")
    private static final String CREATED_QUEUE = "";

    @RabbitListener(queues = CREATED_QUEUE)
    public void consumeMessage(MessageTemplate<PaymentCreatedMessage> payload) {
        LOGGER.info("[OrderReceivedConsumer.consumeMessage] - Consuming message from queue. origin:"
                + payload.getOrigin() + ", tracing: " + payload.getTracing() + " - Start");
        this.paymentCreatedServiceUseCase.createPayment(payload);
        LOGGER.info("[OrderReceivedConsumer.consumeMessage] - Consuming message from queue. - End");
    }
}

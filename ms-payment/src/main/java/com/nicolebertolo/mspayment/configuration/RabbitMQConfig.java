package com.nicolebertolo.mspayment.configuration;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queues.order-received-payment-queue}")
    private static final String CREATED_QUEUE = "";
    @Value("${rabbitmq.topics.order-received-payment-exchange}")
    private static final String CREATED_TOPIC = "";
    @Value("${rabbitmq.routingKey}")
    private static final String ROUTING_KEY = "";

    @Bean
    Queue createQueue() {
        return new Queue(CREATED_QUEUE, false);
    }

    @Bean
    TopicExchange createExchange() {
        return new TopicExchange(CREATED_TOPIC);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }


    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
}

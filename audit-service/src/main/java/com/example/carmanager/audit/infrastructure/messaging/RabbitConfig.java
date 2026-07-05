package com.example.carmanager.audit.infrastructure.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE = "car-service-manager.events";
    public static final String QUEUE = "audit-log.queue";

    @Bean
    TopicExchange domainEventsExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    Queue auditQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }

    @Bean
    Binding auditBinding(Queue auditQueue, TopicExchange domainEventsExchange) {
        return BindingBuilder.bind(auditQueue).to(domainEventsExchange).with("#");
    }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

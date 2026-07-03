package com.example.carmanager.audit.infrastructure;

import org.springframework.amqp.core.*;
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
}

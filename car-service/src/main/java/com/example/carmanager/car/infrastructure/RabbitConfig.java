package com.example.carmanager.car.infrastructure;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    TopicExchange domainEventsExchange() {
        return new TopicExchange("car-service-manager.events", true, false);
    }
}

package com.example.carmanager.service.infrastructure.messaging;

import com.example.carmanager.service.domain.event.DomainEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitEventPublisher {
    private static final String EXCHANGE = "car-service-manager.events";
    private final RabbitTemplate rabbitTemplate;

    public RabbitEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(String routingKey, DomainEvent event) {
        rabbitTemplate.convertAndSend(EXCHANGE, routingKey, event);
    }
}

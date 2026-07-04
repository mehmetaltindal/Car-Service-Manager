package com.example.carmanager.service;

import com.example.carmanager.service.domain.InvalidStatusTransitionException;
import com.example.carmanager.service.domain.ServiceStatus;
import com.example.carmanager.service.domain.ServiceStatusTransitionPolicy;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class ServiceStatusTransitionPolicyTest {
    @Test
    void allowsOnlyForwardSingleStepTransitions() {
        assertThatCode(() -> ServiceStatusTransitionPolicy.requireValid(ServiceStatus.PENDING, ServiceStatus.IN_PROGRESS))
                .doesNotThrowAnyException();
        assertThatCode(() -> ServiceStatusTransitionPolicy.requireValid(ServiceStatus.IN_PROGRESS, ServiceStatus.DONE))
                .doesNotThrowAnyException();
    }

    @Test
    void rejectsSkippingAndBackwardsTransitions() {
        assertThatCode(() -> ServiceStatusTransitionPolicy.requireValid(ServiceStatus.PENDING, ServiceStatus.DONE))
                .isInstanceOf(InvalidStatusTransitionException.class);
        assertThatCode(() -> ServiceStatusTransitionPolicy.requireValid(ServiceStatus.DONE, ServiceStatus.IN_PROGRESS))
                .isInstanceOf(InvalidStatusTransitionException.class);
    }
}

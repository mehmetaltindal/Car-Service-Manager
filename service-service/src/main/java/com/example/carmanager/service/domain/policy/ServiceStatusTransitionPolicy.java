package com.example.carmanager.service.domain.policy;

import com.example.carmanager.service.domain.enums.ServiceStatus;
import com.example.carmanager.service.domain.exception.InvalidStatusTransitionException;

public final class ServiceStatusTransitionPolicy {
    private ServiceStatusTransitionPolicy() {
    }

    public static boolean canTransition(ServiceStatus current, ServiceStatus next) {
        return (current == ServiceStatus.PENDING && next == ServiceStatus.IN_PROGRESS)
                || (current == ServiceStatus.IN_PROGRESS && next == ServiceStatus.DONE);
    }

    public static void requireValid(ServiceStatus current, ServiceStatus next) {
        if (!canTransition(current, next)) {
            throw new InvalidStatusTransitionException(
                    "Invalid service status transition attempted: " + current + " -> " + next
                            + ". Skipping states is not allowed.");
        }
    }
}

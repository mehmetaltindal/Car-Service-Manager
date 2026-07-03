# Decisions

## ADR-001: Spring Boot Backend

Use Spring Boot for each backend microservice because the design uses Java-style domain models, JPA optimistic locking, RabbitMQ, actuator healthchecks, and Testcontainers.

## ADR-002: MySQL With Testcontainers

Use MySQL for runtime and integration tests. Locking and transaction behavior must be validated against the same database family used locally.

## ADR-003: RabbitMQ Event Bus

Use RabbitMQ topic exchange `car-service-manager.events` for domain events. `audit-service` consumes from `audit-log.queue`.

## ADR-004: React + Tailwind Frontend

Use React and Tailwind for a lightweight operational UI. Keep reusable table, form, and status controls small and focused.

## ADR-005: Monorepo

Use one repository for services, frontend, Compose, and docs so local development and agent workflow are consistent.

## ADR-006: ServiceAction Internal Name

Keep `ServiceAction` as the internal model for applied work while preserving `/api/services` externally.

## ADR-007: Owner Required, Technical Profile Optional

Require `CarOwner` on car creation. Allow `CarTechnicalProfile` to be added later to keep intake lightweight.

# Architecture

## Service Boundaries

`car-service` owns vehicle identity and technical profile data. No other service writes car data.

`service-service` owns service catalog entries and applied service actions. It stores `carId` and optional `carLicensePlate` as references, not as owned car entities.

`audit-service` owns immutable audit event history.

## Domain Model

- `CarOwner`: required owner information for a car.
- `Car`: license plate, brand, model, and owner.
- `CarTechnicalProfile`: optional long-lived technician context.
- `Service`: catalog item such as Oil Change.
- `ServiceAction`: real work performed on a car.
- `ServiceStatus`: `PENDING`, `IN_PROGRESS`, `DONE`.

## Event Flow

Command services publish domain events to `car-service-manager.events`.

`audit-service` consumes all events from `audit-log.queue` and writes `audit_log`.

## Clean Architecture Rules

- `domain` contains entities, enums, policies, and domain exceptions.
- `application` contains use cases, request orchestration, DTO mapping, and command-level rules.
- `infrastructure` contains repositories, RabbitMQ configuration, persistence adapters, and seeders.
- `interfaces` contains REST controllers and exception handlers.

## Dependency Direction

Controllers call application services. Application services coordinate domain objects and infrastructure ports. Domain classes do not depend on controllers or web DTOs.

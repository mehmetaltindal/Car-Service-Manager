# Car Service Manager

Car Service Manager is a domain-driven microservice system for managing cars, owners, technical profiles, service catalog entries, and real service actions performed by technicians.

## Architecture

The system uses Spring Boot microservices, MySQL, RabbitMQ, React, Tailwind, and Docker Compose.

- `car-service` owns `Car`, `CarOwner`, and `CarTechnicalProfile`.
- `service-service` owns `Service` catalog data and `ServiceAction` records.
- `audit-service` consumes domain events and writes `audit_log`.
- `frontend` exposes the operational UI.
- RabbitMQ carries domain events through `car-service-manager.events`.

## Microservices

`car-service` handles car CRUD, required owner data, optional technical profile data, license plate validation, duplicate plate conflicts, operation logs, and car events.

`service-service` handles service catalog reads, service action commands, status transitions, optimistic locking, max 2 `IN_PROGRESS` actions per car, operation logs, and service action events.

`audit-service` consumes all domain events from `audit-log.queue`, persists them, and writes standardized application logs.

## How To Run

```bash
docker compose up --build
```

Frontend: `http://localhost:3000`

RabbitMQ management: `http://localhost:15672`

## How To Test

```bash
mvn test
npm --prefix frontend run build
```

Integration and concurrency tests should use MySQL Testcontainers so transaction and locking behavior matches runtime behavior.

## API Endpoints

- `GET /api/cars`
- `POST /api/cars`
- `PUT /api/cars/{id}`
- `GET /api/services`
- `POST /api/services`
- `PUT /api/services/{id}`
- `GET /api/services/catalog`

`/api/services` returns applied `ServiceAction` records. `/api/services/catalog` returns selectable service types.

## Domain Model

- `CarOwner` has many `Car` records.
- `Car` has one optional `CarTechnicalProfile`.
- `Service` is catalog data.
- `ServiceAction` is real work performed on a car.
- `ServiceStatus` values are `PENDING`, `IN_PROGRESS`, and `DONE`.

## Concurrency Strategy

`ServiceAction` uses JPA `@Version`. Update requests must include `version`; stale updates return `409 Conflict`.

The max 2 `IN_PROGRESS` rule is enforced inside one transaction by locking service action rows for the same car before accepting a transition into `IN_PROGRESS`.

## RabbitMQ Audit Flow

Domain events are published to `car-service-manager.events` with routing keys such as `car.created`, `service-action.updated`, and `service-action.status-changed`.

`audit-service` binds `audit-log.queue` to all routing keys and persists event metadata plus JSON payload.

## Assumptions

Authentication, authorization, API gateway, service discovery, distributed tracing, outbox, saga, CQRS, notifications, analytics, retry, and DLQ are intentionally out of scope for the first implementation.

## Trade-offs

The first version prioritizes clear domain rules, simple local orchestration, and testable concurrency. The event publisher writes directly to RabbitMQ, so guaranteed delivery through an outbox is a future improvement.

## Future Improvements

- Add outbox pattern for reliable event publication.
- Add retry and DLQ strategy for audit consumption.
- Add API gateway and authentication.
- Add OpenTelemetry tracing.
- Split service database users and schemas more strictly in Compose.

# Tasks

## car-service

- Add Testcontainers integration tests for car creation, duplicate plate `409`, and update.
- Add pagination support to `GET /api/cars`.
- Add dedicated technical profile update endpoint if UI needs partial updates.

Acceptance: API contract remains DTO-based and duplicate plates never create a second car.

## service-service

- Add Testcontainers integration tests for service action creation, filters, invalid transitions, optimistic locking conflict, and max-2 concurrency.
- Verify row-level locking behavior under MySQL with concurrent requests.
- Add recent technician notes endpoint if frontend needs an unfiltered history query.

Acceptance: stale update returns `409`, invalid transition returns `400`, and `IN_PROGRESS` count never exceeds 2.

## audit-service

- Add RabbitMQ integration test proving consumed events are persisted to `audit_log`.
- Add idempotency strategy if duplicate event delivery becomes a requirement.

Acceptance: every consumed event creates one audit row and writes the standardized log message.

## frontend

- Add component tests for validation errors, conflict refresh behavior, valid next status options, and filters.
- Add pagination controls after backend pagination is available.

Acceptance: user-visible errors come from backend responses and conflict handling refreshes stale rows.

## DevOps

- Run `docker compose up --build` smoke test.
- Add CI workflow after repository hosting is selected.

Acceptance: all services become healthy and frontend can execute create car, create service action, status update, and audit verification flow.

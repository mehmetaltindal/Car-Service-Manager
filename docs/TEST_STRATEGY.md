# Test Strategy

## Unit Tests

- License plate validator.
- `ServiceStatusTransitionPolicy`.
- DTO mappers.
- Error handling.
- `finishedAt` rule.

## Integration Tests

Use Spring Boot integration tests with Testcontainers MySQL.

Required scenarios:

- Create car.
- Duplicate license plate returns `409`.
- Update car.
- Create service action.
- Filter service actions by `carId`.
- Filter service actions by `status`.
- Invalid status transition returns `400`.
- Optimistic locking conflict returns `409`.
- RabbitMQ consumer writes `audit_log`.

## Concurrency Tests

Optimistic locking:

- Load the same `ServiceAction` in two sessions.
- Update the first copy successfully.
- Update the second stale copy.
- Assert the second update returns `409`.

Max-2 active rule:

- Create one car reference.
- Create multiple pending `ServiceAction` rows.
- Run concurrent requests moving actions to `IN_PROGRESS`.
- Assert the final `IN_PROGRESS` count never exceeds 2.

## Frontend Tests

- Validation errors render.
- Conflict message renders and row refresh is triggered.
- Status dropdown only shows valid next states.
- Service action list filters by car and status.

## Smoke Test

Run:

```bash
docker compose up --build
```

Then verify:

- backend actuator health endpoints are `UP`.
- frontend loads.
- create car works.
- create service action works.
- status update works.
- audit row is persisted.

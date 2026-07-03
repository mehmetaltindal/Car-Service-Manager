# Work Items

## WI-001: Monorepo Foundation

Status: Completed

Scope:

- Root Maven parent.
- Backend module folders.
- Frontend folder.
- Docker Compose baseline.
- Core documentation folder.

Completion evidence:

- `pom.xml`
- `car-service/pom.xml`
- `service-service/pom.xml`
- `audit-service/pom.xml`
- `docker-compose.yml`

## WI-002: car-service Core

Status: Completed

Scope:

- Car, owner, and technical profile domain model.
- License plate validation.
- Duplicate plate conflict handling.
- DTO-based REST API.
- Operation log.
- RabbitMQ domain events.

Completion evidence:

- `GET /api/cars`
- `POST /api/cars`
- `PUT /api/cars/{id}`
- `LicensePlateValidatorTest`

## WI-003: service-service Core

Status: Completed

Scope:

- Service catalog.
- ServiceAction model.
- Status transition policy.
- Optimistic locking version contract.
- Max-2 `IN_PROGRESS` transaction guard.
- DTO-based `/api/services` API.
- Operation log.
- RabbitMQ domain events.

Completion evidence:

- `GET /api/services`
- `POST /api/services`
- `PUT /api/services/{id}`
- `GET /api/services/catalog`
- `ServiceStatusTransitionPolicyTest`

## WI-004: audit-service Core

Status: Completed

Scope:

- RabbitMQ queue binding.
- Domain event consumer.
- `audit_log` persistence.
- Standardized application log.

Completion evidence:

- `AuditEventConsumer`
- `audit_log` entity.

## WI-005: Frontend Operations UI

Status: Completed

Scope:

- Car creation form.
- Car table.
- Service action creation form.
- Service action table.
- Status filter.
- Valid next-status dropdown.
- Optimistic-lock conflict message and refresh behavior.
- Technician context panel.

Completion evidence:

- `frontend/src/App.tsx`

## WI-006: Workflow Documentation

Status: Completed

Scope:

- README.
- Architecture docs.
- API contract.
- Domain rules.
- Test strategy.
- Runbook.
- Agent workflow.
- Work item tracking.
- Next action tracking.
- Engineering rules.

Completion evidence:

- `docs/`

## WI-007: Verification And Hardening

Status: In Progress

Scope:

- Run Maven tests. Completed with `mvn -pl car-service,service-service,audit-service test`.
- Run frontend build. Blocked until npm dependency install completes.
- Add Testcontainers integration tests.
- Run Docker Compose smoke test.
- Commit and push each completed work item when a remote exists.

Completion evidence:

- Passing test/build output.
- Commit hashes recorded in `docs/PROJECT_STATUS.md`.

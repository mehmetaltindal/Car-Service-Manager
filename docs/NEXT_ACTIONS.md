# Next Actions

## Immediate

1. Retry `npm --prefix frontend install --no-audit --no-fund` in an environment where npm can complete.
2. Run `npm --prefix frontend run build`.
3. Fix any frontend compile/build issues.
4. Run `docker compose up --build`.
5. Record smoke-test results in `docs/PROJECT_STATUS.md`.

## Required Test Expansion

1. Add Testcontainers integration tests for `car-service`.
2. Add Testcontainers integration tests for `service-service`.
3. Add concurrency test for optimistic locking.
4. Add concurrency test for max-2 `IN_PROGRESS`.
5. Add audit consumer integration test.
6. Add frontend component tests.

## Git And Delivery

1. Work only on branches named `feature-[development-description]`.
2. Commit after each completed work item.
3. Push after each commit when a remote is configured.
4. Record commit hashes in `docs/PROJECT_STATUS.md`.

## Product Improvements

1. Add backend pagination.
2. Add dedicated technical profile update endpoint.
3. Add recent technician notes endpoint.
4. Add CI pipeline.
5. Add outbox and DLQ strategy when reliability requirements increase.

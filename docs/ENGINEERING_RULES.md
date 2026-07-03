# Engineering Rules

## Branch Naming

All development branches must use:

```text
feature-[development-description]
```

Examples:

- `feature-initial-car-service-manager`
- `feature-service-action-concurrency-tests`
- `feature-technician-context-ui`

Do not use `main`, `master`, `develop`, or ad hoc branch names for feature work.

## Commit And Push

- Split work into logical work items tracked in `docs/WORK_ITEMS.md`.
- Commit after each completed work item.
- Push after each commit when a Git remote exists.
- If push cannot run because no remote exists, record that limitation in `docs/PROJECT_STATUS.md`.

## Clean Code

- Keep methods small and focused.
- Avoid duplicated validation, mapping, and transition logic.
- Use expressive names from the domain language.
- Do not add abstractions unless they remove real duplication or protect a domain boundary.
- Keep comments rare and useful.

## Clean Architecture

- Controllers must not contain business rules.
- JPA entities must not be returned directly from controllers.
- Domain rules belong in domain entities, value objects, or policy classes.
- Application services orchestrate use cases and transaction boundaries.
- Infrastructure code must not leak into API DTOs.

## SOLID

- Single Responsibility: each class should have one reason to change.
- Open/Closed: add behavior through focused policies or services, not scattered conditionals.
- Liskov Substitution: avoid inheritance unless substitutability is clear.
- Interface Segregation: keep ports narrow.
- Dependency Inversion: higher-level policies should not depend on low-level implementation details.

## DDD

- Use ubiquitous language from the design document.
- Keep `Service` catalog and `ServiceAction` applied work distinct.
- Keep `CarTechnicalProfile` separate from `Car` identity.
- Keep bounded context ownership clear.
- Do not share database tables across services.

## Testing

- Every domain rule requires a unit test.
- Every persistence or concurrency rule requires an integration test against MySQL.
- Failed verification must be recorded with the exact command and reason.

# Agent Workflow

Every implementation pass must follow this loop:

1. Read `docs/PROJECT_STATUS.md`.
2. Read `docs/WORK_ITEMS.md`, `docs/NEXT_ACTIONS.md`, and `docs/ENGINEERING_RULES.md`.
3. Inspect relevant code before editing.
4. State the intended change.
5. Implement the smallest coherent slice.
6. Run focused tests or record why tests could not run.
7. Review changed files for duplication, domain leakage, and broken contracts.
8. Update `docs/PROJECT_STATUS.md`, `docs/WORK_ITEMS.md`, and `docs/NEXT_ACTIONS.md`.
9. Commit the completed work item on a `feature-[development-description]` branch and push when a remote exists.

## Completion Rule

A task is not complete until one of these is true:

- Relevant tests pass.
- A specific test limitation is recorded with the exact command that could not run.

## Code Quality Rule

- Keep domain rules in domain policies or entities.
- Do not expose JPA entities from controllers.
- Avoid repeated mapping and validation logic.
- Prefer small application services over fat controllers.
- Keep dependencies pointing inward: interfaces and infrastructure depend on application/domain, not the reverse.

## Review Loop

Before final response, verify:

- API contract is still compatible.
- Operation log and audit log responsibilities are not mixed.
- `Service` catalog and `ServiceAction` applied work remain distinct.
- Status transitions still go through `ServiceStatusTransitionPolicy`.
- New docs reflect actual code.

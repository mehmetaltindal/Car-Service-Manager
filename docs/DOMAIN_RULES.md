# Domain Rules

## License Plate

Use:

```regex
^[A-Z0-9][A-Z0-9 -]{1,15}[A-Z0-9]$
```

Accepted characters are uppercase letters, digits, spaces, and hyphens. The first and last character must be a letter or digit.

Duplicate plate returns `409 Conflict`.

## Service Status

Allowed transitions:

- `PENDING -> IN_PROGRESS`
- `IN_PROGRESS -> DONE`

Rejected transitions:

- `PENDING -> DONE`
- `IN_PROGRESS -> PENDING`
- `DONE -> IN_PROGRESS`
- same-state updates as status transitions.

The only place that owns this rule is `ServiceStatusTransitionPolicy`.

## finishedAt

`finishedAt` is set only when status becomes `DONE`.

For `PENDING` and `IN_PROGRESS`, `finishedAt` must be `null`.

## Owner And Technical Profile

`CarOwner` is required on car creation.

`CarTechnicalProfile` is optional and contains technician context, not car identity.

## Operation Log vs Audit Log

Operation logs record command outcomes inside the service that executed the command. They include success, validation errors, conflicts, and failures.

Audit logs record successful domain events consumed by `audit-service`.

# API Contract

## Cars

### `GET /api/cars`

Returns all cars.

### `POST /api/cars`

Creates a car. `owner` is required. `technicalProfile` is optional.

Duplicate license plate returns:

```json
{
  "status": 409,
  "error": "Conflict",
  "message": "License plate already exists: 34 ABC 123"
}
```

### `PUT /api/cars/{id}`

Updates car identity, owner fields, and optional technical profile.

## Services

### `GET /api/services`

Returns service actions. Optional filters:

- `carId`
- `status`

### `GET /api/services/catalog`

Returns service catalog items for the create action form.

### `POST /api/services`

Creates a `ServiceAction` in `PENDING` status.

### `PUT /api/services/{id}`

Updates status and technician report. Request must include `version`.

Invalid transition returns `400`.

Stale version or max-2 active violation returns `409`.

## DTO Policy

JPA entities are never exposed directly. Controllers accept request DTOs and return response DTOs.

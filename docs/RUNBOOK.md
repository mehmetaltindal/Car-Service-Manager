# Runbook

## Local Startup

```bash
docker compose up --build
```

Open:

- Frontend: `http://localhost:3000`
- RabbitMQ: `http://localhost:15672`

Default RabbitMQ credentials are `guest` / `guest`.

## Common Failures

### MySQL Is Not Healthy

Check container logs:

```bash
docker compose logs mysql
```

Confirm no local MySQL is already using port `3306`, or override `MYSQL_PORT`.

### RabbitMQ Is Not Healthy

Check:

```bash
docker compose logs rabbitmq
```

Confirm ports `5672` and `15672` are free, or override the environment variables.

### Backend Cannot Connect To MySQL

Verify `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, and `SPRING_DATASOURCE_PASSWORD`.

### Frontend Cannot Reach APIs

In Docker, nginx proxies `/api/cars` to `car-service:8081` and `/api/services` to `service-service:8082`.

In local Vite dev mode, `vite.config.ts` proxies to localhost ports `8081` and `8082`.

## Reset

```bash
docker compose down -v
docker compose up --build
```

This removes MySQL data and recreates the system from scratch.

# Operasyon Rehberi

## Lokal Başlatma

```bash
docker compose up --build
```

Açılacak adresler:

- Frontend: `http://localhost:3000`
- RabbitMQ: `http://localhost:15672`

Varsayılan RabbitMQ kullanıcı bilgisi `guest` / `guest`.

## Yaygın Hatalar

### MySQL Healthy Değil

Container loglarını kontrol et:

```bash
docker compose logs mysql
```

Lokal makinede `3306` portunu kullanan başka MySQL olmadığını doğrula veya `MYSQL_PORT` değerini override et.

### RabbitMQ Healthy Değil

Kontrol et:

```bash
docker compose logs rabbitmq
```

`5672` ve `15672` portlarının boş olduğunu doğrula veya environment variable değerlerini override et.

### Backend MySQL’e Bağlanamıyor

`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` ve `SPRING_DATASOURCE_PASSWORD` değerlerini doğrula.

### Frontend API’lere Ulaşamıyor

Docker içinde nginx `/api/cars` isteklerini `car-service:8081` adresine, `/api/services` isteklerini `service-service:8082` adresine proxy eder.

Lokal Vite dev mode içinde `vite.config.ts`, localhost `8081` ve `8082` portlarına proxy eder.

## Reset

```bash
docker compose down -v
docker compose up --build
```

Bu işlem MySQL datasını siler ve sistemi sıfırdan oluşturur.

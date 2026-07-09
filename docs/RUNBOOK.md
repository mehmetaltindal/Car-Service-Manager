# Operasyon Rehberi

## Lokal Başlatma

Temiz bir clone sonrası ön koşullar:

- Docker Desktop veya Docker Engine + Compose plugin çalışır durumda olmalı.
- Makine internetten base image ve npm/Maven bağımlılıklarını çekebilmeli veya ilgili Docker layer cache hazır olmalı.
- Varsayılan `3000`, `3306`, `5672`, `15672`, `8081`, `8082` ve `8083` portları boş olmalı.

```bash
docker compose up --build
```

Açılacak adresler:

- Frontend: `http://localhost:3000`
- RabbitMQ: `http://localhost:15672`

Varsayılan RabbitMQ kullanıcı bilgisi `guest` / `guest`.

Port çakışması varsa örnek override:

```bash
FRONTEND_PORT=3001 MYSQL_PORT=3307 docker compose up --build
```

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

## API Smoke Test

Compose ortamı ayaktayken uçtan uca API ve audit doğrulamasını çalıştır:

```bash
scripts/docker-compose-api-smoke.sh
```

Bu script araç oluşturur, servis katalog kaydını kullanarak servis aksiyonu oluşturur, status değerini `IN_PROGRESS` yapar, `carId/status` filtresini doğrular ve `audit_service.audit_log` içinde ilgili eventlerin kalıcılaştığını kontrol eder.

## Reset

```bash
docker compose down -v
docker compose up --build
```

Bu işlem MySQL datasını siler ve sistemi sıfırdan oluşturur.

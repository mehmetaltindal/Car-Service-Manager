# Car Service Manager

Car Service Manager; araçları, araç sahiplerini, teknik profilleri, servis kataloglarını ve teknisyenlerin araçlara uyguladığı gerçek servis işlemlerini yönetmek için tasarlanmış domain odaklı bir microservice sistemidir.

## Mimari

Sistem Spring Boot microservice servisleri, MySQL, RabbitMQ, React, Tailwind ve Docker Compose kullanır.

- `car-service`, `Car`, `CarOwner` ve `CarTechnicalProfile` verilerinin sahibidir.
- `service-service`, `Service` katalog verilerinin ve `ServiceAction` kayıtlarının sahibidir.
- `audit-service`, domain eventlerini tüketir ve `audit_log` tablosuna yazar.
- `frontend`, operasyonel kullanıcı arayüzünü sağlar.
- RabbitMQ, domain eventlerini `car-service-manager.events` exchange’i üzerinden taşır.

## Microservice Servisleri

`car-service`; araç CRUD işlemleri, zorunlu araç sahibi bilgisi, opsiyonel teknik profil, plaka doğrulama, duplicate plaka çakışmaları, operation log ve araç eventlerinden sorumludur.

`service-service`; servis katalog okumaları, servis aksiyonu komutları, status geçişleri, optimistic locking, araç başına en fazla 2 adet `IN_PROGRESS` aksiyon kuralı, operation log ve servis aksiyonu eventlerinden sorumludur.

`audit-service`; `audit-log.queue` kuyruğundan tüm domain eventlerini tüketir, kalıcı hale getirir ve standart application log yazar.

## Çalıştırma

```bash
docker compose up --build
```

Frontend: `http://localhost:3000`

RabbitMQ yönetim ekranı: `http://localhost:15672`

## Test

```bash
mvn test
npm --prefix frontend run build
```

Integration ve concurrency testleri MySQL Testcontainers ile çalışmalıdır. Böylece transaction ve locking davranışı runtime ortamıyla aynı veritabanı ailesinde doğrulanır.

## API Endpointleri

- `GET /api/cars`
- `POST /api/cars`
- `PUT /api/cars/{id}`
- `GET /api/services`
- `POST /api/services`
- `PUT /api/services/{id}`
- `GET /api/services/catalog`

`/api/services`, araca uygulanmış `ServiceAction` kayıtlarını döner. `/api/services/catalog`, seçim yapılabilecek servis tiplerini döner.

## Domain Modeli

- `CarOwner`, birden fazla `Car` kaydına sahip olabilir.
- `Car`, opsiyonel bir `CarTechnicalProfile` kaydına sahiptir.
- `Service`, katalog bilgisidir.
- `ServiceAction`, araç üzerinde yapılan gerçek servis işidir.
- `ServiceStatus` değerleri `PENDING`, `IN_PROGRESS` ve `DONE` şeklindedir.

## Concurrency Stratejisi

`ServiceAction`, JPA `@Version` kullanır. Update requestleri `version` göndermek zorundadır; stale update durumunda `409 Conflict` döner.

Araç başına en fazla 2 adet `IN_PROGRESS` kuralı, aynı transaction içinde ilgili araçtaki servis aksiyonu satırları kilitlenerek uygulanır.

## RabbitMQ Audit Akışı

Domain eventleri `car-service-manager.events` exchange’ine `car.created`, `service-action.updated`, `service-action.status-changed` gibi routing keylerle publish edilir.

`audit-service`, `audit-log.queue` kuyruğunu tüm routing keylere bind eder ve event metadata bilgisiyle JSON payload’u kalıcı hale getirir.

## Varsayımlar

Authentication, authorization, API gateway, service discovery, distributed tracing, outbox, saga, CQRS, notification, analytics, retry ve DLQ ilk sürüm kapsamı dışındadır.

## Trade-off’lar

İlk sürüm domain kurallarının netliğini, basit lokal orkestrasyonu ve test edilebilir concurrency davranışını önceliklendirir. Event publisher doğrudan RabbitMQ’ya yazar; outbox ile garantili teslimat sonraki iyileştirmedir.

## Gelecek İyileştirmeler

- Güvenilir event yayını için outbox pattern eklemek.
- Audit tüketimi için retry ve DLQ stratejisi eklemek.
- API gateway ve authentication eklemek.
- OpenTelemetry tracing eklemek.
- Compose içinde servis veritabanı kullanıcılarını ve schema yetkilerini daha sıkı ayırmak.

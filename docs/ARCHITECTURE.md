# Mimari

## Servis Sınırları

`car-service`, araç kimliği ve teknik profil verilerinin sahibidir. Başka hiçbir servis araç verisini yazmaz.

`service-service`, servis katalog kayıtlarının ve araca uygulanmış servis aksiyonlarının sahibidir. `carId` ve opsiyonel `carLicensePlate` bilgisini referans olarak saklar; bunları sahip olduğu araç entityleri gibi modellemez.

`audit-service`, değiştirilemez audit event geçmişinin sahibidir.

## Domain Modeli

- `CarOwner`: araç için zorunlu sahip bilgisi.
- `Car`: plaka, marka, model ve sahip bilgisi.
- `CarTechnicalProfile`: opsiyonel, uzun ömürlü teknisyen context bilgisi.
- `Service`: Oil Change gibi katalog kalemi.
- `ServiceAction`: araç üzerinde yapılan gerçek iş.
- `ServiceStatus`: `PENDING`, `IN_PROGRESS`, `DONE`.

## Event Akışı

Command servisleri domain eventlerini `car-service-manager.events` exchange’ine publish eder.

`audit-service`, tüm eventleri `audit-log.queue` kuyruğundan tüketir ve `audit_log` tablosuna yazar.

## Clean Architecture Kuralları

- `api/controller`: REST controller sınıflarını içerir.
- `api/dto`: public request/response DTO contract sınıflarını içerir.
- `api/exception`: REST exception handler sınıflarını içerir.
- `application/service`: use case orchestration ve transaction boundary sınıflarını içerir.
- `application/mapper`: DTO-domain dönüşüm sınıflarını içerir.
- `application/exception`: command seviyesinde kullanılan application exception sınıflarını içerir.
- `domain/entity`: JPA entity ve aggregate sınıflarını içerir.
- `domain/enums`: domain enumlarını içerir.
- `domain/event`: domain event contract sınıflarını içerir.
- `domain/policy`: domain policy ve rule sınıflarını içerir.
- `domain/exception`: domain exception sınıflarını içerir.
- `infrastructure/persistence`: repository ve persistence adapter sınıflarını içerir.
- `infrastructure/messaging`: RabbitMQ publisher, consumer config ve message converter sınıflarını içerir.
- `infrastructure/seed`: seed ve bootstrap sınıflarını içerir.

## Dependency Yönü

`api/controller` katmanı application service çağırır. Application service sınıfları domain nesnelerini ve infrastructure portlarını koordine eder. Domain sınıfları API DTO’larına, controller’a veya infrastructure detaylarına bağımlı olmaz.

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

- `domain`: entity, enum, policy ve domain exception sınıflarını içerir.
- `application`: use case, request orchestration, DTO mapping ve command seviyesi kuralları içerir.
- `infrastructure`: repository, RabbitMQ configuration, persistence adapter ve seed sınıflarını içerir.
- `interfaces`: REST controller ve exception handler sınıflarını içerir.

## Dependency Yönü

Controller katmanı application service çağırır. Application service sınıfları domain nesnelerini ve infrastructure portlarını koordine eder. Domain sınıfları controller veya web DTO’larına bağımlı olmaz.

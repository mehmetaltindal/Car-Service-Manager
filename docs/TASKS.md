# Görevler

## car-service

- Araç oluşturma, duplicate plaka `409` ve araç güncelleme için Testcontainers integration testleri ekle.
- `GET /api/cars` için pagination desteği ekle.
- UI partial update ihtiyacı duyarsa ayrı technical profile update endpoint’i ekle.

Kabul kriteri: API contract DTO tabanlı kalır ve duplicate plaka ikinci araç oluşturmaz.

## service-service

- Servis aksiyonu oluşturma, filtreler, geçersiz transition, optimistic locking conflict ve max-2 concurrency için Testcontainers integration testleri ekle.
- MySQL altında concurrent requestlerle row-level locking davranışını doğrula.
- Frontend filtreden bağımsız geçmiş sorgusuna ihtiyaç duyarsa recent technician notes endpoint’i ekle.

Kabul kriteri: stale update `409`, invalid transition `400` döner ve `IN_PROGRESS` sayısı hiçbir zaman 2’yi aşmaz.

## audit-service

- Consume edilen eventlerin `audit_log` tablosuna yazıldığını kanıtlayan RabbitMQ integration test’i ekle.
- Duplicate event delivery requirement olursa idempotency stratejisi ekle.

Kabul kriteri: Her consume edilen event bir audit row oluşturur ve standart log mesajı yazılır.

## frontend

- Validation error, conflict refresh davranışı, valid next status seçenekleri ve filtreler için component testleri ekle.
- Backend pagination hazır olduğunda pagination kontrollerini ekle.

Kabul kriteri: Kullanıcıya gösterilen hatalar backend response mesajlarından gelir ve conflict handling stale satırları refresh eder.

## DevOps

- `docker compose up --build` smoke testini çalıştır.
- Repository hosting netleşince CI workflow ekle.

Kabul kriteri: Tüm servisler healthy olur; frontend create car, create service action, status update ve audit doğrulama akışını çalıştırır.

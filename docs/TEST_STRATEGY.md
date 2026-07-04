# Test Stratejisi

## Unit Testler

- Plaka doğrulayıcı.
- `ServiceStatusTransitionPolicy`.
- DTO mapper sınıfları.
- Hata yönetimi.
- `finishedAt` kuralı.

## Integration Testler

Spring Boot integration testleri MySQL Testcontainers ile çalışmalıdır.

Zorunlu senaryolar:

- Araç oluşturma.
- Duplicate plaka `409` döner.
- Araç güncelleme.
- Servis aksiyonu oluşturma.
- Servis aksiyonlarını `carId` ile filtreleme.
- Servis aksiyonlarını `status` ile filtreleme.
- Geçersiz durum geçişi `400` döner.
- Optimistic locking conflict `409` döner.
- RabbitMQ consumer `audit_log` tablosuna yazar.

## Concurrency Testleri

Optimistic locking:

- Aynı `ServiceAction` iki farklı session içinde yüklenir.
- İlk kopya başarıyla güncellenir.
- İkinci stale kopya güncellenmeye çalışılır.
- İkinci update’in `409` döndüğü doğrulanır.

Max-2 active rule:

- Tek bir car reference oluşturulur.
- Birden fazla `PENDING` durumunda `ServiceAction` satırı oluşturulur.
- Aksiyonları eş zamanlı olarak `IN_PROGRESS` durumuna taşıyan istekler çalıştırılır.
- Final `IN_PROGRESS` sayısının hiçbir zaman 2’yi aşmadığı doğrulanır.

## Frontend Testleri

- Validation error mesajları ekrana basılır.
- Conflict mesajı ekrana basılır ve satır refresh tetiklenir.
- Durum seçimi sadece geçerli sonraki durum seçeneklerini gösterir.
- Servis aksiyonu listesi araç ve durum ile filtrelenir.

## Smoke Test

Çalıştır:

```bash
docker compose up --build
```

Sonra doğrula:

- Backend actuator health endpointleri `UP` döner.
- Frontend yüklenir.
- Araç oluşturma çalışır.
- Servis aksiyonu oluşturma çalışır.
- Durum güncelleme akışı çalışır.
- Audit satırı kalıcı hale gelir.

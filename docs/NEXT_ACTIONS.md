# Sonraki Aksiyonlar

## Hemen Yapılacaklar

1. Remote push erişimini tamamla ve mevcut branch’i push et.
2. Npm’in tamamlanabildiği bir ortamda `npm --prefix frontend install --no-audit --no-fund` komutunu tekrar dene.
3. `npm --prefix frontend run build` çalıştır.
4. Frontend compile/build hatası varsa düzelt.
5. `docker compose up --build` çalıştır.
6. Smoke test sonuçlarını `docs/PROJECT_STATUS.md` içine kaydet.

## Gerekli Test Genişletmeleri

1. `car-service` için Testcontainers integration testleri ekle.
2. `service-service` için Testcontainers integration testleri ekle.
3. Optimistic locking için concurrency test ekle.
4. Max-2 `IN_PROGRESS` kuralı için concurrency test ekle.
5. Audit consumer integration test ekle.
6. Frontend component testleri ekle.

## Git ve Teslimat

1. Her zaman `feature-[development-description]` formatında branch kullan.
2. Tamamlanan her iş parçasından sonra commit at.
3. Remote çalışır durumdaysa her committen sonra push et.
4. Commit hashlerini `docs/PROJECT_STATUS.md` içinde kaydet.

## Ürün İyileştirmeleri

1. Backend pagination ekle.
2. Dedicated technical profile update endpoint ekle.
3. Recent technician notes endpoint ekle.
4. CI pipeline ekle.
5. Reliability gereksinimleri artarsa outbox ve DLQ stratejisi ekle.

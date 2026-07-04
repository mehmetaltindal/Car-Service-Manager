# Sonraki Aksiyonlar

## Hemen Yapılacaklar

1. Docker agent rehberine göre `docker compose up --build` smoke testini çalıştır.
2. Smoke test sırasında DB veya container hatası çıkarsa işi doğru agent rehberine yönlendir.
3. Smoke test sonuçlarını `docs/PROJECT_STATUS.md` içine kaydet.
4. Zorunlu Testcontainers integration/concurrency testlerini ekle.

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
3. Her committen sonra feature branch’i remote’a push et.
4. Feature branch remote’a çıktıktan sonra `main` branch’e merge et.
5. Merge sonrası `main` branch’i remote’a push et.
6. Commit, push ve merge hashlerini `docs/PROJECT_STATUS.md` içinde kaydet.
7. Remote veya merge engeli varsa nedeni `docs/PROJECT_STATUS.md` içine yaz.

## Agent Koordinasyonu

1. Frontend işleri için `docs/FRONTEND_AGENT.md` rehberini kullan.
2. Backend işleri için `docs/BACKEND_AGENT.md` rehberini kullan.
3. Docker container işleri için `docs/DOCKER_AGENT.md` rehberini kullan.
4. Veritabanı işleri için `docs/DATABASE_AGENT.md` rehberini kullan.
5. Integration, RabbitMQ ve uçtan uca test işleri için `docs/INTEGRATION_AGENT.md` rehberini kullan.
6. Bir iş bittikten sonra agent en mantıklı yeni işi seçip `docs/PROJECT_STATUS.md` içine kaydeder.

## Ürün İyileştirmeleri

1. Backend pagination ekle.
2. Dedicated technical profile update endpoint ekle.
3. Recent technician notes endpoint ekle.
4. CI pipeline ekle.
5. Reliability gereksinimleri artarsa outbox ve DLQ stratejisi ekle.

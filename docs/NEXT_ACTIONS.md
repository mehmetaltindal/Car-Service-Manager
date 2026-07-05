# Sonraki Aksiyonlar

## Hemen Yapılacaklar

1. Integration agent rehberine göre Testcontainers integration testlerini ekle.
2. Önce `car-service` create/update/duplicate plate integration testlerini tamamla.
3. Ardından `service-service` optimistic locking ve max-2 `IN_PROGRESS` concurrency testlerini ekle.
4. Audit consumer integration testini ekle.
5. Her implementation pass sonunda `docs/PROJECT_STATUS.md` ve ilgili iş parçacığını güncelle.

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

1. Her agent önce `docs/AGENT_WORKFLOW.md` içindeki token optimizasyonlu okuma sırasına uyar.
2. Frontend işleri için sadece gerekli ortak bölümler ve `docs/FRONTEND_AGENT.md` rehberi kullanılır.
3. Backend işleri için sadece gerekli ortak bölümler ve `docs/BACKEND_AGENT.md` rehberi kullanılır.
4. Docker container işleri için sadece gerekli ortak bölümler ve `docs/DOCKER_AGENT.md` rehberi kullanılır.
5. Veritabanı işleri için sadece gerekli ortak bölümler ve `docs/DATABASE_AGENT.md` rehberi kullanılır.
6. Integration, RabbitMQ ve uçtan uca test işleri için sadece gerekli ortak bölümler ve `docs/INTEGRATION_AGENT.md` rehberi kullanılır.
7. Destek dokümanları yalnızca işin kararı veya kabul kriteri için gerekiyorsa okunur.
8. Bir iş bittikten sonra agent en mantıklı yeni işi seçip `docs/PROJECT_STATUS.md` içine kaydeder.

## Ürün İyileştirmeleri

1. Backend pagination ekle.
2. Dedicated technical profile update endpoint ekle.
3. Recent technician notes endpoint ekle.
4. CI pipeline ekle.
5. Reliability gereksinimleri artarsa outbox ve DLQ stratejisi ekle.

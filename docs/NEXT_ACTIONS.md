# Sonraki Aksiyonlar

## Hemen Yapılacaklar

1. CI pipeline ekle.
2. CI içinde backend unit testlerini çalıştır.
3. CI içinde frontend test ve production build komutlarını çalıştır.
4. CI içinde Docker Compose config doğrulamasını çalıştır.
5. Docker runner erişimi varsa `scripts/docker-compose-api-smoke.sh` komutunu CI smoke kapısına ekle.
6. Her implementation pass sonunda `docs/PROJECT_STATUS.md` ve ilgili iş parçacığını güncelle.

## Gerekli Test Genişletmeleri

1. CI pipeline ekle.
2. CI ortamında Docker daemon mevcutsa Docker Compose API smoke testini otomatik çalıştır.

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

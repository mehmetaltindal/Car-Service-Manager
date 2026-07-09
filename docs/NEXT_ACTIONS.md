# Sonraki Aksiyonlar

## Hemen Yapılacaklar

1. Recent technician notes endpoint ekle.
2. Her implementation pass sonunda `docs/PROJECT_STATUS.md` ve ilgili iş parçacığını güncelle.

## Gerekli Test Genişletmeleri

1. CI manuel Docker Compose smoke job’unun ilk GitHub Actions çalışmasını kontrol et.
2. Reliability gereksinimleri artarsa outbox ve DLQ testlerini ekle.

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

1. Recent technician notes endpoint ekle.
2. Reliability gereksinimleri artarsa outbox ve DLQ stratejisi ekle.

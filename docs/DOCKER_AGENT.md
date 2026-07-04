# Docker Agent Rehberi

## Okuma Sırası

1. `docs/PROJECT_STATUS.md`
2. `docs/ENGINEERING_RULES.md`
3. `docs/AGENT_WORKFLOW.md`
4. `docs/NEXT_ACTIONS.md`
5. `docs/RUNBOOK.md`
6. `docs/ARCHITECTURE.md`
7. `docs/INTEGRATION_AGENT.md`
8. Gerekiyorsa `docs/DATABASE_AGENT.md`
9. Bu dosya

## Sorumluluk Alanı

- `docker-compose.yml`
- Servis Dockerfile dosyaları.
- Container build, network, port, environment variable ve healthcheck ayarları.
- MySQL ve RabbitMQ container orchestration.
- Frontend nginx container ve API proxy wiring.

## Çalışma Kuralları

- Hardcoded credential, port veya host ekleme; environment variable kullan.
- Healthcheck olmadan yeni service ekleme.
- Container bağımlılıklarını `depends_on` ve healthcheck ile açık hale getir.
- Docker Compose değişikliği DB init veya schema davranışını etkiliyorsa `DATABASE_AGENT.md` de okunmalıdır.
- Docker smoke test çalıştırılamazsa tam komut, hata ve ortam limiti `docs/PROJECT_STATUS.md` içine yazılmalıdır.

## Tamamlama Kriterleri

- İlgili unit testler başarılı geçmiştir.
- Docker değişikliği için mümkünse `docker compose config` veya `docker compose up --build` doğrulaması yapılmıştır.
- Container değişikliği service API contract’ını bozmaz.
- Commit, feature branch push, `main` merge ve `main` push yapılır; yapılamazsa blokaj kaydedilir.

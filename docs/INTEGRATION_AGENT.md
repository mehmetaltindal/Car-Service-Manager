# Integration Agent Rehberi

## Okuma Sırası

1. `docs/PROJECT_STATUS.md`
2. `docs/ENGINEERING_RULES.md`
3. `docs/AGENT_WORKFLOW.md`
4. `docs/ARCHITECTURE.md`
5. `docs/API_CONTRACT.md`
6. `docs/DOMAIN_RULES.md`
7. `docs/TEST_STRATEGY.md`
8. `docs/RUNBOOK.md`
9. Bu dosya

## Sorumluluk Alanı

- Docker Compose, MySQL, RabbitMQ ve service healthcheck akışı.
- Testcontainers integration testleri.
- Optimistic locking ve max-2 `IN_PROGRESS` concurrency testleri.
- RabbitMQ audit consumer doğrulaması.
- Uçtan uca smoke test.

## Çalışma Kuralları

- Concurrency testleri gerçek MySQL davranışını doğrulamalıdır.
- Docker veya dependency indirme sorunları tam komut ve hata çıktısıyla kaydedilmelidir.
- Smoke test sonuçları `docs/PROJECT_STATUS.md` içinde tarih ve komutla tutulmalıdır.
- Testleri hızlandırmak için domain kuralını zayıflatma veya production davranışından sapma yaratma.

## Tamamlama Kriterleri

- `mvn test`, frontend build ve `docker compose up --build` sonuçları kayıtlıdır.
- Integration/concurrency testleri gerçekçi transaction ve locking davranışını doğrular.
- Değişiklik commitlenir, feature branch remote’a push edilir ve `main` branch’e merge edilir; yapılamazsa blokaj kaydedilir.

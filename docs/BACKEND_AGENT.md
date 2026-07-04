# Backend Agent Rehberi

## Okuma Sırası

1. `docs/PROJECT_STATUS.md`
2. `docs/ENGINEERING_RULES.md`
3. `docs/AGENT_WORKFLOW.md`
4. `docs/ARCHITECTURE.md`
5. `docs/DOMAIN_RULES.md`
6. `docs/API_CONTRACT.md`
7. `docs/TEST_STRATEGY.md`
8. Bu dosya

## Sorumluluk Alanı

- `car-service/`, `service-service/` ve `audit-service/` Spring Boot servisleri.
- Domain entity, policy, application service, DTO, repository ve REST controller katmanları.
- RabbitMQ event publish/consume akışı.
- Operation log ve audit log ayrımı.

## Çalışma Kuralları

- JPA entityleri controller response olarak döndürme.
- Domain kurallarını controller içine yazma.
- `Service` katalog modeli ile `ServiceAction` gerçek işlem modelini karıştırma.
- Status geçişleri sadece `ServiceStatusTransitionPolicy` üzerinden ilerlemelidir.
- Optimistic locking ve max-2 `IN_PROGRESS` kuralını transaction güvenliğiyle koru.
- API contract değişikliği gerekiyorsa `docs/API_CONTRACT.md`, frontend etkisi ve test etkisi aynı işte güncellenmelidir.

## Tamamlama Kriterleri

- İlgili unit veya integration testleri çalışır veya neden çalışmadığı kayıtlıdır.
- Domain kuralları için test eklenmiştir.
- Değişiklik commitlenir, feature branch remote’a push edilir ve `main` branch’e merge edilir; yapılamazsa blokaj kaydedilir.

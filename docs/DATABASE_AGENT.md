# Database Agent Rehberi

## Okuma Sırası

1. `docs/PROJECT_STATUS.md`
2. `docs/ENGINEERING_RULES.md`
3. `docs/AGENT_WORKFLOW.md`
4. `docs/ARCHITECTURE.md`
5. `docs/DOMAIN_RULES.md`
6. `docs/TEST_STRATEGY.md`
7. `docs/RUNBOOK.md`
8. Gerekiyorsa `docs/DOCKER_AGENT.md`
9. Bu dosya

## Sorumluluk Alanı

- MySQL schema ve database ownership kuralları.
- Docker MySQL init scriptleri.
- JPA persistence davranışı.
- Testcontainers database setup.
- Seed data ve veri migration kararları.

## Çalışma Kuralları

- Servisler arasında tablo paylaşımı yapma.
- Her servis kendi data ownership sınırını korumalıdır.
- Concurrency ve locking davranışını H2 ile değil MySQL/Testcontainers ile doğrula.
- Migration veya schema değişikliği API ve domain kurallarıyla birlikte değerlendirilmelidir.
- Veri kaybı yaratabilecek işlem için önce runbook ve rollback notu ekle.

## Tamamlama Kriterleri

- İlgili unit testler başarılı geçmiştir.
- Persistence veya concurrency davranışı değiştiyse MySQL/Testcontainers integration testi eklenmiştir veya blokaj kayıt altındadır.
- Docker init veya DB config değişikliği Docker agent ile koordine edilmiştir.
- Commit, feature branch push, `main` merge ve `main` push yapılır; yapılamazsa blokaj kaydedilir.

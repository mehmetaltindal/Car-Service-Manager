# İş Parçacıkları

## WI-001: Monorepo Temeli

Durum: Tamamlandı

Kapsam:

- Root Maven parent.
- Backend module klasörleri.
- Frontend klasörü.
- Docker Compose temeli.
- Ana dokümantasyon klasörü.

Tamamlanma kanıtı:

- `pom.xml`
- `car-service/pom.xml`
- `service-service/pom.xml`
- `audit-service/pom.xml`
- `docker-compose.yml`

## WI-002: car-service Core

Durum: Tamamlandı

Kapsam:

- Car, araç sahibi ve teknik profil domain modeli.
- Plaka doğrulama.
- Duplicate plaka conflict yönetimi.
- DTO tabanlı REST API.
- Operation log.
- RabbitMQ domain eventleri.

Tamamlanma kanıtı:

- `GET /api/cars`
- `POST /api/cars`
- `PUT /api/cars/{id}`
- `LicensePlateValidatorTest`

## WI-003: service-service Core

Durum: Tamamlandı

Kapsam:

- Service katalog.
- ServiceAction modeli.
- Durum geçiş policy’si.
- Optimistic locking version contract.
- Max-2 `IN_PROGRESS` transaction guard.
- DTO tabanlı `/api/services` API.
- Operation log.
- RabbitMQ domain eventleri.

Tamamlanma kanıtı:

- `GET /api/services`
- `POST /api/services`
- `PUT /api/services/{id}`
- `GET /api/services/catalog`
- `ServiceStatusTransitionPolicyTest`

## WI-004: audit-service Core

Durum: Tamamlandı

Kapsam:

- RabbitMQ queue binding.
- Domain event consumer.
- `audit_log` persistence.
- Standart application log.

Tamamlanma kanıtı:

- `AuditEventConsumer`
- `audit_log` entity.

## WI-005: Frontend Operasyon UI

Durum: Tamamlandı

Kapsam:

- Araç oluşturma formu.
- Araç tablosu.
- Servis aksiyonu oluşturma formu.
- Servis aksiyonu tablosu.
- Durum filtresi.
- Geçerli next-status dropdown.
- Optimistic-lock conflict mesajı ve refresh davranışı.
- Technician context panel.

Tamamlanma kanıtı:

- `frontend/src/App.tsx`

## WI-006: Workflow Dokümantasyonu

Durum: Tamamlandı

Kapsam:

- README.
- Mimari dokümanları.
- API contract.
- Domain kuralları.
- Test stratejisi.
- Operasyon rehberi.
- Agent workflow.
- İş parçacığı takibi.
- Sonraki aksiyon takibi.
- Mühendislik kuralları.

Tamamlanma kanıtı:

- `docs/`

## WI-007: Doğrulama ve Sertleştirme

Durum: Devam Ediyor

Kapsam:

- Maven testlerini çalıştır. `mvn -pl car-service,service-service,audit-service test` ile tamamlandı.
- Frontend build çalıştır. Npm dependency kurulumu tamamlanana kadar blokeli.
- Testcontainers integration testlerini ekle.
- Docker Compose smoke test çalıştır.
- Remote erişimi çalışır hale geldiğinde tamamlanan her iş parçasını commit ve push et.

Tamamlanma kanıtı:

- Passing test/build çıktısı.
- Commit hashleri `docs/PROJECT_STATUS.md` içinde kayıtlı.

## WI-008: Dokümantasyonu Türkçeleştirme

Durum: Tamamlandı

Kapsam:

- Tüm Markdown dosyalarını Türkçeye çevir.
- Kod isimleri, endpointler, enum değerleri ve komutları contract olarak koru.
- Bariz İngilizce başlık ve takip etiketlerini Türkçeye çevir.

Tamamlanma kanıtı:

- `README.md`
- `docs/*.md`
- `git diff --check`

## WI-009: Agent Koordinasyon Kuralları

Durum: Tamamlandı

Kapsam:

- Her iş sonrası en mantıklı yeni işi seçme kuralını ekle.
- Her iş sonrası feature branch, commit, push ve `main` merge kuralını netleştir.
- Multi-agent okuma sırasını tanımla.
- Frontend, backend ve integration agent rehberlerini oluştur.

Tamamlanma kanıtı:

- `docs/AGENT_WORKFLOW.md`
- `docs/ENGINEERING_RULES.md`
- `docs/NEXT_ACTIONS.md`
- `docs/FRONTEND_AGENT.md`
- `docs/BACKEND_AGENT.md`
- `docs/INTEGRATION_AGENT.md`

## WI-010: Unit Test Kabul Kuralı

Durum: Tamamlandı

Kapsam:

- Hiçbir işin unit testler başarılı geçmeden tamamlanmış sayılmayacağını netleştir.
- Kabul kriterini başarılı unit test sonucu olarak tanımla.
- Başarısız unit testlerin skip edilmesini yasakla.

Tamamlanma kanıtı:

- `docs/AGENT_WORKFLOW.md`
- `docs/ENGINEERING_RULES.md`
- `docs/TEST_STRATEGY.md`
- `docs/PROJECT_STATUS.md`

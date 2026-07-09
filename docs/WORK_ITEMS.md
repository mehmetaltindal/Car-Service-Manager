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
- Frontend build çalıştır. `npm --prefix frontend run build` ile tamamlandı.
- Testcontainers integration testlerini ekle.
- Docker Compose smoke test çalıştır. `scripts/docker-compose-api-smoke.sh` ile API ve audit akışı doğrulandı.
- Remote erişimi çalışır hale geldiğinde tamamlanan her iş parçasını commit ve push et.

Tamamlanma kanıtı:

- Passing test/build çıktısı.
- Commit hashleri `docs/PROJECT_STATUS.md` içinde kayıtlı.

## WI-013: Docker Compose Smoke Test Sertleştirmesi

Durum: Tamamlandı

Kapsam:

- Backend Dockerfile Maven reactor build context düzeltmesi.
- Spring Boot executable jar üretimi.
- MySQL init SQL dosyasını bind mount yerine özel MySQL image içine alma.
- Docker build context için `.dockerignore`.
- Backend healthcheck komutlarını runtime image ile uyumlu hale getirme.
- Frontend healthcheck varlık kontrolü.
- Frontend healthcheck adresini container içinde `127.0.0.1` olarak sabitleme.

Tamamlanma kanıtı:

- `docker compose config` başarılı.
- `docker compose up --build --detach` sonrası MySQL, RabbitMQ, `car-service`, `service-service` ve `audit-service` healthy oldu.
- `mvn -pl car-service,service-service,audit-service test` başarılı: 4 test, 0 skipped.
- Host `curl` doğrulaması kullanım limiti ve sandbox ağ kısıtı nedeniyle tamamlanamadı; kısıt `docs/PROJECT_STATUS.md` içinde kayıtlı.

## WI-019: Docker Compose API Smoke Test Genişletmesi

Durum: Tamamlandı

Kapsam:

- Docker Compose ortamı üzerinde uçtan uca API smoke scripti.
- `POST /api/cars` ile araç oluşturma.
- `GET /api/services/catalog` ile katalog doğrulama.
- `POST /api/services` ile servis aksiyonu oluşturma.
- `PUT /api/services/{id}` ile `IN_PROGRESS` status update doğrulama.
- `GET /api/services?carId=...&status=IN_PROGRESS` filtre doğrulama.
- MySQL içindeki `audit_log` tablosunda car ve service action eventlerinin kalıcılaştığını doğrulama.

Tamamlanma kanıtı:

- `scripts/docker-compose-api-smoke.sh`
- `docker compose up --build --detach`
- `scripts/docker-compose-api-smoke.sh`: `Smoke test passed: car=2, serviceAction=2, auditEvents=3`

## WI-020: CI Pipeline

Durum: Tamamlandı

Kapsam:

- GitHub Actions workflow.
- Push ve pull request için backend unit test.
- Push ve pull request için frontend component testleri.
- Push ve pull request için frontend production build.
- Push ve pull request için Docker Compose config doğrulaması.
- Manuel çalıştırılan Docker Compose API smoke job’u.

Tamamlanma kanıtı:

- `.github/workflows/ci.yml`

## WI-021: Frontend Türkçeleştirme

Durum: Tamamlandı

Kapsam:

- Ekranda görünen frontend label, başlık, placeholder, buton ve tablo başlıkları.
- Servis durumlarının Türkçe gösterimi.
- Backend hata mesajlarının kullanıcıya Türkçe gösterimi.
- Katalog servis adlarının ekranda Türkçe gösterimi.
- HTML `lang` ve sayfa başlığı.

Tamamlanma kanıtı:

- `frontend/src/App.tsx`
- `frontend/src/App.test.tsx`
- `frontend/index.html`
- `npm --prefix frontend test`
- `npm --prefix frontend run build`

## WI-022: Backend Pagination

Durum: Tamamlandı

Kapsam:

- `GET /api/cars` için opsiyonel `page` ve `size` parametreleri.
- `GET /api/services` için mevcut `carId/status` filtreleriyle uyumlu opsiyonel `page` ve `size` parametreleri.
- Response body liste formatını koruyan geriye uyumlu contract.
- Pagination metadata headerları: `X-Total-Count`, `X-Page`, `X-Size`, `X-Total-Pages`.
- Integration testlerle pagination contract doğrulaması.

Tamamlanma kanıtı:

- `car-service/src/test/java/com/example/carmanager/car/CarIntegrationIT.java`
- `service-service/src/test/java/com/example/carmanager/service/ServiceActionIntegrationIT.java`
- `mvn -pl car-service clean verify`
- `mvn -pl service-service clean verify`

## WI-023: Dedicated Technical Profile Update Endpoint

Durum: Tamamlandı

Kapsam:

- `PUT /api/cars/{id}/technical-profile` endpoint’i.
- Araç kimliği, model, marka ve sahip bilgisini değiştirmeden teknik profil güncelleme.
- Teknik profil güncelleme için operation log ve domain event üretimi.
- Integration test ile endpoint contract doğrulaması.

Tamamlanma kanıtı:

- `car-service/src/main/java/com/example/carmanager/car/api/controller/CarController.java`
- `car-service/src/main/java/com/example/carmanager/car/application/service/CarApplicationService.java`
- `car-service/src/test/java/com/example/carmanager/car/CarIntegrationIT.java`
- `mvn -pl car-service clean verify`

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

## WI-011: Frontend Dependency ve Build Doğrulaması

Durum: Tamamlandı

Kapsam:

- Frontend npm dependency kurulumunu tamamla.
- `frontend/package-lock.json` üret.
- Frontend production build komutunu çalıştır.

Tamamlanma kanıtı:

- `npm --prefix frontend install --no-audit --no-fund --loglevel=info`
- `npm --prefix frontend run build`
- `frontend/package-lock.json`

## WI-012: Docker ve Database Agent Rehberleri

Durum: Tamamlandı

Kapsam:

- Docker container işleri için ayrı agent rehberi oluştur.
- Veritabanı işleri için ayrı agent rehberi oluştur.
- Agent workflow içinde işleri doğru agent rehberine yönlendirme kuralını güncelle.

Tamamlanma kanıtı:

- `docs/DOCKER_AGENT.md`
- `docs/DATABASE_AGENT.md`
- `docs/AGENT_WORKFLOW.md`
- `docs/NEXT_ACTIONS.md`

## WI-014: car-service Integration Testleri

Durum: Tamamlandı

Kapsam:

- `car-service` için Testcontainers MySQL integration test altyapısı.
- `POST /api/cars` başarılı create senaryosu.
- Duplicate license plate için `409 Conflict` senaryosu.
- `PUT /api/cars/{id}` public contract update senaryosu.
- Testcontainers 2.0.5 ve Docker Engine 29 uyumluluğu.
- Runtime parameter name bağımlılığını kaldıran açık `@PathVariable("id")` kullanımı.

Tamamlanma kanıtı:

- `car-service/src/test/java/com/example/carmanager/car/CarIntegrationIT.java`
- `mvn -pl car-service clean verify`: 2 unit test ve 3 integration test başarılı, 0 skipped.
- `mvn -pl car-service,service-service,audit-service test`: 4 unit test başarılı, 0 skipped.

## WI-015: service-service Integration ve Concurrency Testleri

Durum: Tamamlandı

Kapsam:

- `service-service` için Testcontainers MySQL integration test altyapısı.
- `POST /api/services` service action create senaryosu.
- `GET /api/services?carId=...` ve `GET /api/services?status=...` filtre senaryoları.
- Invalid status transition için `400 Bad Request` senaryosu.
- Stale optimistic locking version için `409 Conflict` senaryosu.
- `finishedAt` değerinin sadece `DONE` durumunda set edilmesi.
- Concurrent `IN_PROGRESS` transition denemelerinde max-2 kuralının gerçek MySQL row-level locking ile korunması.

Tamamlanma kanıtı:

- `service-service/src/test/java/com/example/carmanager/service/ServiceActionIntegrationIT.java`
- `mvn -pl service-service clean verify`: 2 unit test ve 6 integration/concurrency test başarılı, 0 skipped.
- `mvn -pl car-service,service-service,audit-service test`: 4 unit test başarılı, 0 skipped.

## WI-016: audit-service RabbitMQ Integration Testi

Durum: Tamamlandı

Kapsam:

- `audit-service` için Testcontainers MySQL ve RabbitMQ integration test altyapısı.
- RabbitMQ exchange/queue binding üzerinden domain event consume senaryosu.
- `audit_log` persistence doğrulaması.
- JSON message converter ile event payload aktarımı.

Tamamlanma kanıtı:

- `audit-service/src/test/java/com/example/carmanager/audit/AuditEventConsumerIntegrationIT.java`
- `mvn -pl audit-service clean verify`: 1 integration test başarılı, 0 skipped.
- `mvn -pl car-service,service-service,audit-service test`: 4 unit test başarılı, 0 skipped.

## WI-017: Frontend Component Testleri

Durum: Tamamlandı

Kapsam:

- Frontend unit test script’i.
- Validation error render testi.
- Optimistic-lock conflict mesajı ve refresh davranışı testi.
- Geçerli next-status dropdown testi.
- `carId` ve `status` filtre request testi.
- Vite/esbuild build kilitlenmesini önleyen ikon import düzenlemesi.

Tamamlanma kanıtı:

- `frontend/src/App.test.tsx`
- `frontend/src/test/register.ts`
- `npm --prefix frontend test`: 4 test başarılı, 0 skipped.
- `npm --prefix frontend run build`: başarılı.
- `mvn -pl car-service,service-service,audit-service test`: 4 unit test başarılı, 0 skipped.

## WI-018: Backend Klasör ve Paket Ayrımı

Durum: Tamamlandı

Kapsam:

- Public API katmanını `api/controller`, `api/dto`, `api/exception` altında görünür hale getir.
- Application service, mapper ve exception sınıflarını ayrı alt paketlere taşı.
- Domain entity, enum, event, policy ve exception sınıflarını ayrı alt paketlere taşı.
- Persistence, messaging ve seed infrastructure kodlarını ayrı alt paketlere taşı.
- Mühendislik ve backend agent kurallarını yeni klasör standardıyla güncelle.

Tamamlanma kanıtı:

- `car-service/src/main/java/com/example/carmanager/car/api/`
- `service-service/src/main/java/com/example/carmanager/service/api/`
- `audit-service/src/main/java/com/example/carmanager/audit/domain/entity/`
- `mvn -pl car-service,service-service,audit-service test`: 4 unit test başarılı, 0 skipped.

# Agent Workflow

Her implementasyon geçişi şu döngüyü takip etmelidir:

1. `docs/PROJECT_STATUS.md` dosyasını oku.
2. `docs/WORK_ITEMS.md`, `docs/NEXT_ACTIONS.md` ve `docs/ENGINEERING_RULES.md` dosyalarını oku.
3. İşin alanına göre gerekiyorsa ilgili agent rehberini oku:
   - Frontend işleri: `docs/FRONTEND_AGENT.md`
   - Backend işleri: `docs/BACKEND_AGENT.md`
   - Docker container işleri: `docs/DOCKER_AGENT.md`
   - Veritabanı, schema, migration ve seed işleri: `docs/DATABASE_AGENT.md`
   - Integration, test, RabbitMQ veya uçtan uca işler: `docs/INTEGRATION_AGENT.md`
4. İlgili kodu düzenlemeden önce incele.
5. Yapılacak değişikliği açıkça belirt.
6. En küçük tutarlı iş dilimini implemente et.
7. Unit testleri çalıştır; başarılı geçmeden görevi tamamlanmış sayma.
8. Değişen dosyaları duplication, domain leakage ve kırılan contract açısından gözden geçir.
9. `docs/PROJECT_STATUS.md`, `docs/WORK_ITEMS.md` ve `docs/NEXT_ACTIONS.md` dosyalarını güncelle.
10. Tamamlanan iş parçasını `feature-[development-description]` branch’i üzerinde commit et, remote’a push et ve `main` branch’e merge et.
11. İş tamamlandıktan sonra `docs/NEXT_ACTIONS.md` içinden en mantıklı yeni işi seç ve `docs/PROJECT_STATUS.md` içinde “Sonraki Önerilen İş” alanına kaydet.

## Tamamlama Kuralı

Bir görev, aşağıdaki koşullardan biri sağlanmadan tamamlanmış sayılmaz:

- Unit testler başarılı geçmiştir.
- Başarısız unit testler skip edilmemiştir.
- Unit test çalıştırılamıyorsa görev tamamlanmış sayılmaz; blokaj, çalıştırılamayan tam komutla birlikte `docs/PROJECT_STATUS.md` içine kaydedilir.
- İlgili iş parçası commit edilmiştir.
- Remote erişimi varsa feature branch push edilmiş ve `main` branch’e merge edilmiştir.
- Remote veya merge yapılamıyorsa engel `docs/PROJECT_STATUS.md` içine kaydedilmiştir.

## Sonraki İşi Seçme Kuralı

Her iş tamamlandığında agent durup kullanıcıdan yeni iş beklemez. `docs/NEXT_ACTIONS.md` ve `docs/WORK_ITEMS.md` dosyalarına göre en yüksek değerli ve en az blokeli işi seçer.

Öncelik sırası:

1. Blokeli olmayan doğrulama ve build işleri.
2. Kırık contract, test veya çalıştırma hataları.
3. Zorunlu integration/concurrency testleri.
4. Kullanıcıya doğrudan değer sağlayan frontend/backend küçük iyileştirmeleri.
5. Gelecek iyileştirmeler.

Seçilen yeni iş `docs/PROJECT_STATUS.md` içinde kayıt altına alınmalıdır.

## Multi-Agent Okuma Sırası

Başka agentlar projeye dahil olduğunda önce ortak dosyaları, sonra alan dosyasını okumalıdır.

Ortak okuma sırası:

1. `docs/PROJECT_STATUS.md`
2. `docs/ENGINEERING_RULES.md`
3. `docs/AGENT_WORKFLOW.md`
4. `docs/WORK_ITEMS.md`
5. `docs/NEXT_ACTIONS.md`
6. `docs/ARCHITECTURE.md`
7. `docs/API_CONTRACT.md`
8. `docs/DOMAIN_RULES.md`
9. `docs/TEST_STRATEGY.md`

Alan bazlı ek dosya:

- Frontend agent: `docs/FRONTEND_AGENT.md`
- Backend agent: `docs/BACKEND_AGENT.md`
- Docker agent: `docs/DOCKER_AGENT.md`
- Database agent: `docs/DATABASE_AGENT.md`
- Integration agent: `docs/INTEGRATION_AGENT.md`

## İş Yönlendirme Kuralı

Her iş başlamadan önce işin ana sahibi seçilmelidir.

- UI, form, tablo, frontend build ve kullanıcı etkileşimi işleri `FRONTEND_AGENT.md` rehberine yönlendirilir.
- Spring Boot servisleri, domain modeli, API, RabbitMQ publisher/consumer kodu ve DTO işleri `BACKEND_AGENT.md` rehberine yönlendirilir.
- Dockerfile, Docker Compose, container healthcheck, container network ve image build işleri `DOCKER_AGENT.md` rehberine yönlendirilir.
- MySQL schema, seed, migration, Testcontainers database setup ve veri sahipliği işleri `DATABASE_AGENT.md` rehberine yönlendirilir.
- Uçtan uca doğrulama, concurrency testleri, smoke test, servisler arası akış ve RabbitMQ audit doğrulaması `INTEGRATION_AGENT.md` rehberine yönlendirilir.

Bir iş birden fazla alanı etkiliyorsa önce ana sahibi belirlenir, sonra ilgili diğer agent rehberleri destek dokümanı olarak okunur. Örneğin Docker Compose içindeki MySQL init değişikliği hem Docker hem Database alanıdır; ana sahip Docker ise `DOCKER_AGENT.md`, destek olarak `DATABASE_AGENT.md` okunmalıdır.

## Kod Kalitesi Kuralı

- Domain kurallarını domain policy veya entity içinde tut.
- Controller’dan JPA entity döndürme.
- Mapping, validation ve transition logic tekrarından kaçın.
- Şişman controller yerine küçük application service sınıfları kullan.
- Dependency yönünü içeri doğru koru: interfaces ve infrastructure, application/domain katmanlarına bağımlı olabilir; tersi olmamalıdır.

## Review Döngüsü

Final cevap öncesinde şunları doğrula:

- API contract hâlâ uyumlu mu?
- Operation log ve audit log sorumlulukları karışmış mı?
- `Service` katalog modeli ile `ServiceAction` gerçek işlem modeli ayrı mı?
- Durum geçişleri hâlâ `ServiceStatusTransitionPolicy` üzerinden mi ilerliyor?
- Yeni dokümanlar gerçek kodla uyumlu mu?

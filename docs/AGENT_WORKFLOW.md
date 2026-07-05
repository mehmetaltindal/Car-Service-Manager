# Agent Workflow

Her implementasyon geçişi şu döngüyü takip etmelidir:

1. `docs/PROJECT_STATUS.md` dosyasındaki sadece mevcut aşama, aktif iş, engeller ve sonraki önerilen iş alanlarını oku.
2. `docs/NEXT_ACTIONS.md` dosyasındaki sadece işin kapsamına giren başlığı oku.
3. `docs/ENGINEERING_RULES.md` dosyasındaki sadece branch, commit/push, test ve işin alanıyla ilgili kalite kurallarını oku.
4. İşin alanına göre sadece ilgili agent rehberini oku:
   - Frontend işleri: `docs/FRONTEND_AGENT.md`
   - Backend işleri: `docs/BACKEND_AGENT.md`
   - Docker container işleri: `docs/DOCKER_AGENT.md`
   - Veritabanı, schema, migration ve seed işleri: `docs/DATABASE_AGENT.md`
   - Integration, test, RabbitMQ veya uçtan uca işler: `docs/INTEGRATION_AGENT.md`
5. Destek dokümanı yalnızca karar vermek için gerçekten gerekliyse oku; örneğin API contract değişmiyorsa `docs/API_CONTRACT.md` okunmaz.
6. İlgili kodu düzenlemeden önce incele.
7. Yapılacak değişikliği açıkça belirt.
8. En küçük tutarlı iş dilimini implemente et.
9. Unit testleri çalıştır; başarılı geçmeden görevi tamamlanmış sayma.
10. Değişen dosyaları duplication, domain leakage ve kırılan contract açısından gözden geçir.
11. `docs/PROJECT_STATUS.md`, `docs/WORK_ITEMS.md` ve `docs/NEXT_ACTIONS.md` dosyalarında sadece değişen durumları güncelle.
12. Tamamlanan iş parçasını `feature-[development-description]` branch’i üzerinde commit et, remote’a push et ve `main` branch’e merge et.
13. İş tamamlandıktan sonra `docs/NEXT_ACTIONS.md` içinden en mantıklı yeni işi seç ve `docs/PROJECT_STATUS.md` içinde “Sonraki Önerilen İş” alanına kaydet.

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

## Token Optimizasyonlu Multi-Agent Okuma Sırası

Başka agentlar projeye dahil olduğunda her agent yalnızca kendi işi için gerekli Markdown dosyalarını okumalıdır. Amaç, token tüketimini azaltmak ve karar için gerekli olmayan dokümanları yüklememektir.

Zorunlu kısa okuma:

1. `docs/PROJECT_STATUS.md`: sadece mevcut aşama, aktif iş, engeller ve sonraki önerilen iş.
2. `docs/NEXT_ACTIONS.md`: sadece ilgili iş grubu.
3. `docs/ENGINEERING_RULES.md`: sadece branch, commit/push, test ve ilgili kalite kuralı.
4. `docs/AGENT_WORKFLOW.md`: sadece bu okuma sırası, tamamlama kuralı ve iş yönlendirme kuralı.

Agent bazlı zorunlu tek ek dosya:

- Frontend agent: `docs/FRONTEND_AGENT.md`
- Backend agent: `docs/BACKEND_AGENT.md`
- Docker agent: `docs/DOCKER_AGENT.md`
- Database agent: `docs/DATABASE_AGENT.md`
- Integration agent: `docs/INTEGRATION_AGENT.md`

İhtiyaç halinde okunacak destek dosyaları:

- API endpoint, request/response veya hata contract değişiyorsa `docs/API_CONTRACT.md`.
- Domain kuralı, aggregate veya ubiquitous language değişiyorsa `docs/DOMAIN_RULES.md`.
- Servis sınırı, veri sahipliği veya bağımlılık yönü değişiyorsa `docs/ARCHITECTURE.md`.
- Test kapsamı, test türü veya kabul kriteri değişiyorsa `docs/TEST_STRATEGY.md`.
- Yeni iş parçası açılıyor, kapanıyor veya kapsam ayrıştırılıyorsa `docs/WORK_ITEMS.md`.

Okunmaması gerekenler:

- Frontend agent, sadece UI işi yapıyorsa Docker veya Database rehberlerini okumaz.
- Docker agent, sadece healthcheck veya Dockerfile düzenliyorsa API contract okumaz.
- Database agent, sadece init/migration düzenliyorsa frontend rehberini okumaz.
- Integration agent, sadece smoke sonucu kaydediyorsa domain dokümanını okumaz; domain davranışı test edilecekse okur.

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

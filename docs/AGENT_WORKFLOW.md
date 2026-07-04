# Agent Workflow

Her implementasyon geçişi şu döngüyü takip etmelidir:

1. `docs/PROJECT_STATUS.md` dosyasını oku.
2. `docs/WORK_ITEMS.md`, `docs/NEXT_ACTIONS.md` ve `docs/ENGINEERING_RULES.md` dosyalarını oku.
3. İlgili kodu düzenlemeden önce incele.
4. Yapılacak değişikliği açıkça belirt.
5. En küçük tutarlı iş dilimini implemente et.
6. Odaklı testleri çalıştır veya testlerin neden çalıştırılamadığını kaydet.
7. Değişen dosyaları duplication, domain leakage ve kırılan contract açısından gözden geçir.
8. `docs/PROJECT_STATUS.md`, `docs/WORK_ITEMS.md` ve `docs/NEXT_ACTIONS.md` dosyalarını güncelle.
9. Tamamlanan iş parçasını `feature-[development-description]` branch’i üzerinde commit et ve remote varsa push et.

## Tamamlama Kuralı

Bir görev, aşağıdaki koşullardan biri sağlanmadan tamamlanmış sayılmaz:

- İlgili testler geçmiştir.
- Testin çalıştırılamama nedeni, çalıştırılamayan tam komutla birlikte kaydedilmiştir.

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

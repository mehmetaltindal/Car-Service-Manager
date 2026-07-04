# Mühendislik Kuralları

## Branch İsimlendirme

Tüm geliştirme branchleri şu formatta olmalıdır:

```text
feature-[development-description]
```

Örnekler:

- `feature-initial-car-service-manager`
- `feature-service-action-concurrency-tests`
- `feature-technician-context-ui`

Feature geliştirme için `main`, `master`, `develop` veya rastgele branch isimleri kullanılmamalıdır.

## Commit ve Push

- İşleri `docs/WORK_ITEMS.md` içinde takip edilen mantıklı iş parçacıklarına böl.
- Tamamlanan her iş parçasından sonra commit at.
- Git remote varsa her committen sonra push et.
- Push çalıştırılamazsa nedeni `docs/PROJECT_STATUS.md` içine kaydet.

## Clean Code

- Methodları küçük ve odaklı tut.
- Validation, mapping ve transition logic tekrarından kaçın.
- Domain dilinden gelen anlamlı isimler kullan.
- Gerçek duplication azaltmayan veya domain boundary korumayan abstraction ekleme.
- Yorumları az ve gerçekten faydalı olacak şekilde kullan.

## Clean Architecture

- Controller business rule içermez.
- JPA entityleri controller’dan doğrudan dönülmez.
- Domain kuralları domain entity, value object veya policy sınıflarında durur.
- Application service sınıfları use case ve transaction boundary yönetir.
- Infrastructure kodu API DTO’larına sızmamalıdır.

## SOLID

- Single Responsibility: Her sınıfın değişmek için tek bir nedeni olmalıdır.
- Open/Closed: Davranış eklemek için dağınık conditional yerine odaklı policy veya service kullan.
- Liskov Substitution: Substitutability net değilse inheritance kullanma.
- Interface Segregation: Portları dar tut.
- Dependency Inversion: Üst seviye policyler alt seviye implementasyon detaylarına bağımlı olmamalıdır.

## DDD

- Design dokümanındaki ubiquitous language kullanılmalıdır.
- `Service` katalog modeli ile `ServiceAction` uygulanmış iş modeli ayrı tutulmalıdır.
- `CarTechnicalProfile`, `Car` kimlik bilgisinden ayrı tutulmalıdır.
- Bounded context ownership net olmalıdır.
- Servisler arasında veritabanı tablosu paylaşılmamalıdır.

## Test

- Her domain kuralı unit test gerektirir.
- Her persistence veya concurrency kuralı MySQL’e karşı integration test gerektirir.
- Başarısız doğrulama, tam komut ve gerekçesiyle kaydedilmelidir.

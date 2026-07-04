# Domain Kuralları

## Plaka

Kullanılacak regex:

```regex
^[A-Z0-9][A-Z0-9 -]{1,15}[A-Z0-9]$
```

Kabul edilen karakterler büyük harf, rakam, boşluk ve tiredir. İlk ve son karakter harf veya rakam olmalıdır.

Duplicate plaka durumunda `409 Conflict` döner.

## Servis Durumu

Geçerli geçişler:

- `PENDING -> IN_PROGRESS`
- `IN_PROGRESS -> DONE`

Reddedilen geçişler:

- `PENDING -> DONE`
- `IN_PROGRESS -> PENDING`
- `DONE -> IN_PROGRESS`
- Aynı status değerine yapılan status transition girişimleri.

Bu kuralın tek sahibi `ServiceStatusTransitionPolicy` sınıfıdır.

## finishedAt

`finishedAt` yalnızca status `DONE` olduğunda set edilir.

`PENDING` ve `IN_PROGRESS` durumlarında `finishedAt` `null` olmalıdır.

## Araç Sahibi ve Teknik Profil

`CarOwner`, araç oluşturma sırasında zorunludur.

`CarTechnicalProfile` opsiyoneldir ve araç kimliği değil, teknisyen context bilgisidir.

## Operation Log ve Audit Log

Operation log, komutu çalıştıran servis içinde command sonucunu kaydeder. Başarı, validation error, conflict ve failure durumlarını içerir.

Audit log, `audit-service` tarafından consume edilen başarılı domain event geçmişini kaydeder.

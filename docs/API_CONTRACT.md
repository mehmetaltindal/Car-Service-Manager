# API Contract

## Cars

### `GET /api/cars`

Tüm araçları döner.

Opsiyonel pagination parametreleri:

- `page`: 0 tabanlı sayfa numarası. Varsayılan `0`.
- `size`: sayfa boyutu. Varsayılan `20`, maksimum `100`.

`page` veya `size` verilmezse eski davranış korunur ve tüm araç listesi döner.

Pagination kullanıldığında response body yine liste formatındadır. Metadata header olarak döner:

- `X-Total-Count`
- `X-Page`
- `X-Size`
- `X-Total-Pages`

### `POST /api/cars`

Araç oluşturur. `owner` alanı zorunludur. `technicalProfile` alanı opsiyoneldir.

Duplicate plaka durumunda şu response döner:

```json
{
  "status": 409,
  "error": "Conflict",
  "message": "License plate already exists: 34 ABC 123"
}
```

### `PUT /api/cars/{id}`

Araç kimliği, sahip alanları ve opsiyonel teknik profil bilgisini günceller.

## Services

### `GET /api/services`

Servis aksiyonlarını döner. Opsiyonel filtreler:

- `carId`
- `status`
- `page`: 0 tabanlı sayfa numarası. Varsayılan `0`.
- `size`: sayfa boyutu. Varsayılan `20`, maksimum `100`.

`page` veya `size` verilmezse eski davranış korunur ve filtreye uyan tüm servis aksiyonu listesi döner.

Pagination kullanıldığında response body yine liste formatındadır. Metadata header olarak döner:

- `X-Total-Count`
- `X-Page`
- `X-Size`
- `X-Total-Pages`

### `GET /api/services/catalog`

Servis aksiyonu oluşturma formu için servis katalog kayıtlarını döner.

### `POST /api/services`

`PENDING` durumu ile yeni bir `ServiceAction` oluşturur.

### `PUT /api/services/{id}`

Durum ve teknisyen raporunu günceller. İstek içinde `version` zorunludur.

Geçersiz durum geçişi `400` döner.

Stale version veya max-2 active rule ihlali `409` döner.

## DTO Politikası

JPA entityleri asla doğrudan dışarı açılmaz. Controller’lar istek DTO’su kabul eder ve response DTO döner.

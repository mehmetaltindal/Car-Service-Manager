# Frontend Agent Rehberi

## Okuma Sırası

1. `docs/PROJECT_STATUS.md`
2. `docs/ENGINEERING_RULES.md`
3. `docs/AGENT_WORKFLOW.md`
4. `docs/NEXT_ACTIONS.md`
5. `docs/API_CONTRACT.md`
6. `docs/DOMAIN_RULES.md`
7. `docs/TEST_STRATEGY.md`
8. Bu dosya

## Sorumluluk Alanı

- `frontend/` altındaki React + Tailwind uygulaması.
- Araç listesi, servis aksiyonu listesi, formlar, filtreler ve technician context UI.
- Backend validation ve conflict mesajlarının kullanıcıya doğru gösterilmesi.
- Status dropdown’ın sadece geçerli sonraki durumları göstermesi.

## Çalışma Kuralları

- API contract dışına çıkma; endpoint veya DTO değişikliği gerekiyorsa backend agent ile koordine et.
- UI metinleri Türkçe olmalıdır.
- Form, tablo ve hata durumlarını tekrar kullanılabilir ama gereksiz abstraction üretmeyen componentlerle yönet.
- Optimistic locking conflict durumunda kullanıcıya mesaj göster ve ilgili veriyi refresh et.
- Frontend build veya test çalıştırılamazsa tam komutu ve nedeni `docs/PROJECT_STATUS.md` içine yaz.

## Tamamlama Kriterleri

- İlgili frontend build/test komutları çalışır veya neden çalışmadığı kayıtlıdır.
- UI, domain kurallarını backend ile çelişmeden yansıtır.
- Değişiklik commitlenir, feature branch remote’a push edilir ve `main` branch’e merge edilir; yapılamazsa blokaj kaydedilir.

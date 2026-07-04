# Kararlar

## ADR-001: Spring Boot Backend

Her backend microservice için Spring Boot kullanılacak. Tasarım Java tarzı domain modelleri, JPA optimistic locking, RabbitMQ, actuator healthcheck ve Testcontainers ile uyumludur.

## ADR-002: MySQL ve Testcontainers

Runtime ve integration testleri için MySQL kullanılacak. Locking ve transaction davranışı lokal runtime ile aynı veritabanı ailesinde doğrulanmalıdır.

## ADR-003: RabbitMQ Event Bus

Domain eventleri için RabbitMQ topic exchange `car-service-manager.events` kullanılacak. `audit-service`, `audit-log.queue` kuyruğundan consume eder.

## ADR-004: React + Tailwind Frontend

Hafif operasyonel UI için React ve Tailwind kullanılacak. Table, form ve status kontrolleri küçük ve odaklı tutulacak.

## ADR-005: Monorepo

Servisler, frontend, Compose ve dokümanlar tek repository içinde tutulacak. Böylece lokal geliştirme ve agent workflow tutarlı ilerler.

## ADR-006: ServiceAction İç Model Adı

Araca uygulanmış gerçek iş için iç model adı `ServiceAction` olarak kalacak; dış API contract `/api/services` olarak korunacak.

## ADR-007: Araç Sahibi Zorunlu, Teknik Profil Opsiyonel

Araç oluştururken `CarOwner` zorunlu olacak. `CarTechnicalProfile` sonradan eklenebilir; böylece ilk araç kaydı hafif kalır.

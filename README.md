# AdvancedHibernate.Java26

A production-oriented **Java 26 / Spring Boot 4.1.1 / Hibernate ORM 7.4** reference project that demonstrates advanced
Hibernate ORM and Jakarta Bean Validation patterns while preserving the same Clean Architecture / DDD boundaries used in
`CleanArchitecture.DDD.Java`.

> The project deliberately separates the domain model from JPA entities. `core` and `domain` contain no Spring, Jakarta
> Persistence or Hibernate dependencies.

## Baseline

| Component                   |                      Version / baseline |
|-----------------------------|----------------------------------------:|
| Java                        |                                  **26** |
| Spring Boot                 |                               **4.1.1** |
| Hibernate ORM               |                        **7.4.11.Final** |
| Hibernate Validator         |                         **9.1.4.Final** |
| Jakarta Persistence         | **3.2** (through Hibernate/Spring Boot) |
| Jakarta Validation          |                                 **3.1** |
| Maven                       |                 **3.9.16+ recommended** |
| Default DB                  |       H2, PostgreSQL compatibility mode |
| Production-style DB profile |                              PostgreSQL |

Hibernate 7.4.11.Final is intentionally pinned instead of using the older patch managed by Spring Boot 4.1.1.
Hibernate's 7.4 compatibility matrix lists Spring Boot 4.1 and Java 26 support. Keep this explicit override under
dependency-management review when upgrading Spring Boot.

## Architecture

```text
advanced-hibernate-api
        │
        ▼
advanced-hibernate-infrastructure ─────┐
        │                              │ adapters / Hibernate / JPA
        ▼                              │
advanced-hibernate-application ◄───────┘
        │
        ▼
advanced-hibernate-domain
        │
        ▼
advanced-hibernate-core

code-integrity-tests ── ArchUnit dependency rules
```

### Modules

- **advanced-hibernate-core** — framework-free shared kernel (`AggregateRoot`, domain events, paging).
- **advanced-hibernate-domain** — framework-free DDD aggregate/value objects (`PurchaseOrder`, `Money`, typed IDs).
- **advanced-hibernate-application** — use-case ports, DTOs, Jakarta Validation contracts and custom constraints.
- **advanced-hibernate-infrastructure** — JPA entities, Hibernate-specific mappings, repositories, query showcase,
  Envers, tenant setup, caching.
- **advanced-hibernate-api** — REST API, validation boundary, RFC 9457-style `ProblemDetail`, Swagger/OpenAPI.
- **code-integrity-tests** — ArchUnit rules guarding dependency direction.

## Hibernate feature map

| Feature                              | Demonstration                                                                 |
|--------------------------------------|-------------------------------------------------------------------------------|
| Persistence context / dirty checking | `HibernateShowcaseService.reserveOptimistically()`                            |
| Aggregate mapping                    | `PurchaseOrderJpaEntity` + `OrderLineJpaEntity`                               |
| Owning/inverse associations          | `OrderLineJpaEntity.order` / `PurchaseOrderJpaEntity.lines`                   |
| LAZY to-one / to-many                | customer, product and line mappings                                           |
| `JOIN FETCH`                         | `fetchJoinOrders()`                                                           |
| Named `EntityGraph`                  | `Order.details`                                                               |
| Dynamic `EntityGraph`                | `dynamicEntityGraph()`                                                        |
| N+1 mitigation / batch fetching      | `@BatchSize(size = 32)` + global `default_batch_fetch_size`                   |
| SUBSELECT fetching                   | `CustomerJpaEntity.orders`                                                    |
| DTO projection                       | `JpaOrderRepository.findSummaries()`                                          |
| Embeddables                          | `AddressEmbeddable`                                                           |
| Natural IDs                          | `ProductJpaEntity.sku` + `byNaturalId()`                                      |
| Optimistic locking                   | `@Version` + `reserveOptimistically()`                                        |
| Pessimistic locking                  | `PESSIMISTIC_WRITE` + `reservePessimistically()`                              |
| JDBC batching                        | `hibernate.jdbc.batch_size=50`, sequence allocation, `batchPersistProducts()` |
| Flush / clear                        | `batchPersistProducts()`                                                      |
| Bulk HQL DML                         | `bulkCancelOldDrafts()`                                                       |
| Soft delete                          | `@SoftDelete` on customer/order                                               |
| Dynamic Hibernate filter             | `@FilterDef/@Filter` + `filteredByMinimumTotal()`                             |
| Tenant discriminator                 | `TenantDocumentJpaEntity.@TenantId` + `TenantConfiguration`                   |
| JSON mapping                         | `CustomerPreferences` + `@JdbcTypeCode(SqlTypes.JSON)`                        |
| `AttributeConverter`                 | `EmailAttributeConverter`                                                     |
| Formula                              | order `lineCount` via `@Formula`                                              |
| Column transformation                | `TemperatureReadingJpaEntity.@ColumnTransformer`                              |
| JPA lifecycle callbacks              | `@PrePersist/@PreUpdate` on temperature readings                              |
| SQL inspection hook                  | `StatementInspector` via `SqlInspectionConfiguration`                         |
| Immutable reference entity           | `CategoryJpaEntity.@Immutable`                                                |
| Second-level cache                   | `CategoryJpaEntity.@Cache`; enable `cache` Spring profile                     |
| Envers audit history                 | order + lines + `EnversHistoryService`                                        |
| Inheritance                          | `PaymentJpaEntity` single-table hierarchy                                     |
| Polymorphic HQL / `treat()`          | `cardPayments()`                                                              |
| Criteria API                         | `criteriaProductSearch()`                                                     |
| HQL CTE                              | `expensiveOrders()`                                                           |
| HQL set operations                   | `customerEmailUnion()`                                                        |
| HQL lateral join                     | `mostExpensiveLinePerOrder()`                                                 |
| HQL window functions                 | `AnalyticsAdapter` running revenue + rank                                     |
| Keyset pagination                    | `keysetPage()`                                                                |
| Multi-ID load                        | `byMultipleIds(...).multiLoad()`                                              |
| Native SQL                           | `nativeMonthlyRevenue()`                                                      |
| Read-only transactions               | read query methods                                                            |
| `StatelessSession`                   | `statelessInsertCategories()`                                                 |
| L2/query-cache policy                | L2 opt-in; `cacheableCategories()` demonstrates query-cache hint              |
| First-level cache identity           | `firstLevelCacheIdentity()`                                                   |
| Flush mode                           | `countProductsWithoutQueryTimeAutoFlush()`                                    |
| Vector mapping                       | source-only PostgreSQL example in `examples.hibernate.vector`                 |

### Why the vector example is outside normal entity scanning

`DocumentEmbeddingMappingExample` is intentionally placed in `examples.hibernate.vector`. H2 is the zero-setup default
database and does not represent a production pgvector installation. To make the vector example executable, move it into
the scanned persistence package under a PostgreSQL profile and enable the database's vector extension. This keeps the
default project bootable while still showing the Hibernate 7 vector mapping API.

## Advanced Jakarta Bean Validation feature map

| Validation capability                | Demonstration                                                    |
|--------------------------------------|------------------------------------------------------------------|
| Standard constraints                 | `@NotBlank`, `@Email`, `@Positive`, `@DecimalMin`, `@Size`       |
| Record component validation          | `CreateOrderCommand` and nested `Line` record                    |
| Cascaded validation                  | `List<@Valid Line>`                                              |
| Container-element constraints        | `List<@Email String>`, `Map<@NotBlank String,@Size(...) String>` |
| Validation groups                    | `ValidationGroups.Basic`, `Business`, `ExpensiveChecks`          |
| Ordered group sequence               | `ValidationGroups.OrderedChecks`                                 |
| Field/type-use custom constraint     | `@ValidSku`                                                      |
| Class-level constraint               | `@ConsistentOrderRequest`, `@ValidDateRange`                     |
| Custom property-node violation       | `DateRangeValidator` attaches error to `end`                     |
| Dependency-injected validator        | `@UniqueCustomerEmail` -> `CustomerEmailAvailabilityPort`        |
| Cross-parameter method constraint    | `@ChronologicalParameters`                                       |
| Method validation                    | `ValidationShowcaseService` + `@Validated`                       |
| Return-value validation              | `@NotBlank` on `createReport()` return value                     |
| Programmatic validation              | `ProgrammaticValidationService`                                  |
| Custom value extractor               | `BoxValueExtractor` + `Box<@NotBlank String>`                    |
| Validation metadata API              | `/api/v1/validation/metadata`                                    |
| Validation payload/severity metadata | `Severity.Info` used by `CreateOrderCommand.tags`                |
| API error translation                | `ApiExceptionHandler` -> `ProblemDetail`                         |

### Validation layering used by the project

```text
HTTP/API constraints
        │ malformed request, shape, size, format
        ▼
Jakarta Bean Validation
        │ cross-field / expensive availability checks
        ▼
Application use case
        │ orchestration / permissions / existence
        ▼
DDD aggregate invariants
        │ rules that must never be violated
        ▼
Database constraints
        final integrity boundary
```

The important distinction is deliberate: **Bean Validation does not replace aggregate invariants or database
constraints**.

## Interesting validation examples

### 1. Container element validation

```java
public record NewsletterRequest(
        @NotEmpty List<@Email String> recipients,
        Map<@NotBlank String, @Size(max = 50) String> attributes
) {
}
```

### 2. Ordered validation groups

Cheap structural validation runs first, business-level validation second, and database-backed uniqueness checks last:

```java

@GroupSequence({Basic.class, Business.class, ExpensiveChecks.class})
public interface OrderedChecks {
}
```

API usage:

```java

@PostMapping
ResponseEntity<Void> register(
        @RequestBody @Validated(ValidationGroups.OrderedChecks.class)
        RegisterCustomerCommand command
) { ...}
```

### 3. Dependency-injected custom validator

`@UniqueCustomerEmail` delegates to an application port. The validator does not know about JPA:

```text
UniqueCustomerEmailValidator
        ↓
CustomerEmailAvailabilityPort       application boundary
        ↓
CustomerEmailAvailabilityAdapter    JPA infrastructure
```

### 4. Class-level constraint with precise property path

`@ValidDateRange` checks two values together but reports the violation against `end`, producing a better REST error
contract.

### 5. Cross-parameter method validation

```java

@ChronologicalParameters
@NotBlank
public String createReport(@NotNull Instant from, @NotNull Instant to) { ...}
```

The constraint targets `ValidationTarget.PARAMETERS`; the `@NotBlank` applies to the return value.

## Running

Prerequisites:

```text
JDK 26
Maven 3.9.16+
```

Verify everything:

```bash
mvn clean verify
```

Start the default H2 profile:

```bash
mvn -pl advanced-hibernate-api -am spring-boot:run
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

Health:

```text
http://localhost:8080/actuator/health
```

### PostgreSQL profile

```bash
docker compose up -d postgres
mvn -pl advanced-hibernate-api -am spring-boot:run -Dspring-boot.run.profiles=postgres
```

### Second-level cache profile

The cache is intentionally opt-in so cache invalidation/correctness is a conscious architectural decision:

```bash
mvn -pl advanced-hibernate-api -am spring-boot:run -Dspring-boot.run.profiles=cache
```

Combine profiles:

```bash
-Dspring-boot.run.profiles=postgres,cache
```

## Example requests

Seeded IDs:

```text
Customer: 10000000-0000-0000-0000-000000000001
Product:  20000000-0000-0000-0000-000000000001  BOOK-1001
Product:  20000000-0000-0000-0000-000000000002  DEV-1002
```

Create an order:

```bash
curl -X POST http://localhost:8080/api/v1/orders \
  -H 'Content-Type: application/json' \
  -d '{
    "customerId":"10000000-0000-0000-0000-000000000001",
    "lines":[{
      "productId":"20000000-0000-0000-0000-000000000001",
      "sku":"BOOK-1001",
      "productName":"Hibernate Mastery",
      "quantity":2,
      "unitPrice":49.90,
      "currency":"EUR",
      "notificationEmails":["dev@example.com"]
    }],
    "tags":["learning","hibernate"]
  }'
```

Trigger a validation error:

```bash
curl -X POST http://localhost:8080/api/v1/validation/container-elements \
  -H 'Content-Type: application/json' \
  -d '{"recipients":["not-an-email"],"attributes":{"":"this is still checked"}}'
```

Natural-id lookup:

```bash
curl http://localhost:8080/api/v1/showcase/natural-id/BOOK-1001
```

Optimistic reservation:

```bash
curl -X POST 'http://localhost:8080/api/v1/showcase/inventory/20000000-0000-0000-0000-000000000001/optimistic?quantity=1'
```

Pessimistic reservation:

```bash
curl -X POST 'http://localhost:8080/api/v1/showcase/inventory/20000000-0000-0000-0000-000000000001/pessimistic?quantity=1'
```

## Performance engineering notes

1. Treat the SQL and execution plan as part of the feature design.
2. Prefer LAZY mappings and explicit query fetch plans.
3. Use DTO projections for list/read-model APIs instead of hydrating large managed graphs.
4. A fetch join solves N+1 but can create row multiplication; do not fetch-join arbitrary multiple bags.
5. `@BatchSize`/default batch fetching is useful when associations are initialized across many already-loaded owners.
6. Bulk HQL bypasses the persistence context; clear or synchronize managed state afterwards.
7. JDBC batching works best with sequence/pooled identifiers rather than identity generation.
8. `StatelessSession` is an ETL tool, not a general replacement for managed persistence.
9. L2 caching is explicitly opt-in. Cache only stable/read-mostly data after measuring.
10. Keep `spring.jpa.open-in-view=false`; resolve required data inside the application transaction.

## Production caveats

This is a **reference/showcase repository**, so one project intentionally contains alternatives you would not
necessarily use together in every production service. In particular:

- Choose a tenant strategy deliberately; the demo uses a fixed tenant identifier so `@TenantId` can be explored safely.
- Benchmark batch sizes rather than copying `32`/`50` blindly.
- Use migrations (Flyway/Liquibase) instead of `ddl-auto=update` in production.
- Native SQL is intentionally database-specific.
- Vector search requires the target database extension/type.
- The new Hibernate 7.4 temporal/audited state-management SPI is incubating; Envers is included as the stable, widely
  understood audit-history example.
- Never rely on Java validation alone for uniqueness/concurrency integrity. Keep unique/FK/check constraints in the
  database.

## Java 26 CI

`.github/workflows/ci.yml` installs Temurin Java 26 and runs `mvn -B clean verify`, making the declared runtime baseline
an executable CI contract.

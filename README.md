# 🧪 Spring Transaction Tracer Lab

A hands-on Maven project to explore **Spring Transactions, AOP logging, and EntityManager lifecycle**.

## 🚀 Run
```bash
mvn clean test
```

## 📂 Project Structure
- `AppConfig` → Spring config, transaction management
- `TransactionLoggingAspect` → logs tx begin/commit/rollback
- `UserService`, `OrderService` → transactional methods with different propagation
- `persistence.xml` → H2 in-memory DB

## 🧭 Milestones
1. Configure `EntityManagerFactory` + `PlatformTransactionManager` in `AppConfig`.
2. Implement persistence logic in `UserService` and `OrderService`.
3. Expand `TransactionLoggingAspect` to log:
   - Thread name
   - Propagation type
   - Call stack
4. Add scenarios:
   - REQUIRED vs REQUIRES_NEW
   - Rollback on checked vs unchecked exceptions
   - Read-only transactions
   - Self-invocation edge case
5. Advanced:
   - Nested transactions with H2
   - Mixing manual + Spring tx

Logs will reveal how Spring manages transactions and entity managers internally.

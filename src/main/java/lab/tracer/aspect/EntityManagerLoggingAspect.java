package lab.tracer.aspect;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Aspect
@Component
public class EntityManagerLoggingAspect {


    // Intercept retrieval of the transactional EntityManager used by @PersistenceContext proxies and Spring’s JPA infra
    @Around("execution(jakarta.persistence.EntityManager org.springframework.orm.jpa.EntityManagerFactoryUtils.doGetTransactionalEntityManager(..)) && args(emf,..)")
    public Object aroundDoGetTransactionalEntityManager(ProceedingJoinPoint pjp, EntityManagerFactory emf) throws Throwable {
        boolean txActiveBefore = TransactionSynchronizationManager.isActualTransactionActive();
        Object holderBefore = TransactionSynchronizationManager.getResource(emf);

        log("[EM-Trace] doGetTransactionalEntityManager ENTER"
                + " txActive=" + txActiveBefore
                + ", holderBefore=" + id(holderBefore)
                + ", emfId=" + id(emf)
                + ", thread=" + Thread.currentThread().getName());

        Object out = pjp.proceed();
        EntityManager em = (EntityManager) out;

        boolean txActiveAfter = TransactionSynchronizationManager.isActualTransactionActive();
        Object holderAfter = TransactionSynchronizationManager.getResource(emf);

        String status;
        if (holderBefore != null) {
            status = "existing-bound";
        } else if (holderAfter != null && em != null) {
            status = "newly-created-and-bound";
        } else if (em != null) {
            status = "newly-created-unbound";
        } else {
            status = "null";
        }

        log("[EM-Trace] doGetTransactionalEntityManager EXIT"
                + " status=" + status
                + ", emId=" + id(em)
                + ", emJoined=" + safeJoined(em)
                + ", flushMode=" + safeFlushMode(em)
                + ", holderAfter=" + id(holderAfter)
                + ", txActive=" + txActiveAfter
                + ", thread=" + Thread.currentThread().getName());

        return em;
    }

    // Intercept direct creation from the EntityManagerFactory (e.g., programmatic EM creation)
    @Around("execution(jakarta.persistence.EntityManager jakarta.persistence.EntityManagerFactory+.createEntityManager(..)) && target(emf)")
    public Object aroundCreateEntityManager(ProceedingJoinPoint pjp, EntityManagerFactory emf) throws Throwable {
        boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
        log("[EM-Trace] EMF.createEntityManager ENTER"
                + " txActive=" + txActive
                + ", emfId=" + id(emf)
                + ", thread=" + Thread.currentThread().getName());

        Object out = pjp.proceed();
        EntityManager em = (EntityManager) out;

        log("[EM-Trace] EMF.createEntityManager EXIT"
                + " emId=" + id(em)
                + ", emJoined=" + safeJoined(em)
                + ", flushMode=" + safeFlushMode(em)
                + ", thread=" + Thread.currentThread().getName());

        return em;
    }

    // Optional: observe when EMs are closed
    @Around("execution(void jakarta.persistence.EntityManager.close()) && target(em)")
    public Object aroundClose(ProceedingJoinPoint pjp, EntityManager em) throws Throwable {
        log("[EM-Trace] EntityManager.close ENTER emId=" + id(em) + ", thread=" + Thread.currentThread().getName());
        Object out = pjp.proceed();
        log("[EM-Trace] EntityManager.close EXIT emId=" + id(em) + ", thread=" + Thread.currentThread().getName());
        return out;
    }

    // Helpers
    private static String id(Object o) {
        return (o == null) ? "null" : Integer.toHexString(System.identityHashCode(o));
    }

    private static boolean safeJoined(EntityManager em) {
        try {
            return em != null && em.isJoinedToTransaction();
        } catch (Exception ignored) {
            return false;
        }
    }

    private static String safeFlushMode(EntityManager em) {
        try {
            return (em != null && em.getFlushMode() != null) ? em.getFlushMode().name() : "null";
        } catch (Exception ignored) {
            return "n/a";
        }
    }

    private static void log(String msg) {
        System.out.println(msg);
    }
}


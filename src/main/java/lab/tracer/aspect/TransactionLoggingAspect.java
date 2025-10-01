package lab.tracer.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Aspect
@Component
public class TransactionLoggingAspect {

    @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
    public Object logTransaction(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("[Tracer] Entering: " + pjp.getSignature());
        System.out.println("  - Tx active? " + TransactionSynchronizationManager.isActualTransactionActive());
        try {
            Object result = pjp.proceed();
            System.out.println("[Tracer] Success: " + pjp.getSignature());
            return result;
        } catch (Throwable t) {
            System.out.println("[Tracer] Exception in: " + pjp.getSignature() + " -> " + t);
            throw t;
        } finally {
            System.out.println("  - Tx still active? " + TransactionSynchronizationManager.isActualTransactionActive());
        }
    }
}

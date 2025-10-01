package lab.tracer.aspect;

import jakarta.persistence.EntityManager;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TracingEntityManager implements InvocationHandler {

    private final EntityManager delegate;

    private TracingEntityManager(EntityManager delegate) {
        this.delegate = delegate;
    }

    public static EntityManager wrap(EntityManager em) {
        return (EntityManager) Proxy.newProxyInstance(
                em.getClass().getClassLoader(),
                new Class[]{EntityManager.class},
                new TracingEntityManager(em)
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("close".equals(method.getName())) {
            System.out.println("[EM-Tracer] Closing EntityManager: " + delegate);
        } else if ("joinTransaction".equals(method.getName())) {
            System.out.println("[EM-Tracer] EntityManager joined transaction: " + delegate);
        } else if ("flush".equals(method.getName())) {
            System.out.println("[EM-Tracer] Flushing EntityManager: " + delegate);
        }
        return method.invoke(delegate, args);
    }
}
package lab.tracer.config;

import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "lab.tracer")
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableTransactionManagement
public class AppConfig {
    // TODO: Configure EntityManagerFactory + TransactionManager beans here if using Spring-managed JPA
}

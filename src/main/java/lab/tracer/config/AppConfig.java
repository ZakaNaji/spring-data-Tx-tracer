package lab.tracer.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "lab.tracer")
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableTransactionManagement
public class AppConfig {
    // TODO: Configure EntityManagerFactory + TransactionManager beans here if using Spring-managed JPA

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("test")
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean managerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        HibernateJpaVendorAdapter vendor = new HibernateJpaVendorAdapter();
        vendor.setGenerateDdl(true);
        vendor.setShowSql(true);


        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        jpaProperties.put("org.hibernate.flushMode", "AUTO");

        managerFactoryBean.setJpaVendorAdapter(vendor);
        managerFactoryBean.setPackagesToScan("lab.tracer.entity");
        managerFactoryBean.setDataSource(dataSource());
        managerFactoryBean.setJpaProperties(jpaProperties);
        return managerFactoryBean;
    }

    @Bean
    @Primary
    public EntityManagerFactory tracedEntityManagerFactory(LocalContainerEntityManagerFactoryBean factoryBean) {
        EntityManagerFactory realEmf = factoryBean.getObject();
        return (EntityManagerFactory) Proxy.newProxyInstance(
                realEmf.getClass().getClassLoader(),
                new Class[]{EntityManagerFactory.class},
                (proxy, method, args) -> {
                    if ("createEntityManager".equals(method.getName())) {
                        EntityManager em = (EntityManager) method.invoke(realEmf, args);
                        System.out.println("[EM-Tracer] Created EntityManager: " + em);
                        return lab.tracer.aspect.TracingEntityManager.wrap(em);
                    }
                    return method.invoke(realEmf, args);
                }
        );
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(emf);
        return jpaTransactionManager;
    }
}

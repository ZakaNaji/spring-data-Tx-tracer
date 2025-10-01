package lab.tracer;

import lab.tracer.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        try (var ctx = new AnnotationConfigApplicationContext(AppConfig.class)) {
            System.out.println("Spring Transaction Tracer started with " + ctx.getBeanDefinitionCount() + " beans");
        }
    }
}

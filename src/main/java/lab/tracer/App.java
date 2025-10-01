package lab.tracer;

import lab.tracer.config.AppConfig;
import lab.tracer.entity.User;
import lab.tracer.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        try (var ctx = new AnnotationConfigApplicationContext(AppConfig.class)) {
            System.out.println("Spring Transaction Tracer started with " + ctx.getBeanDefinitionCount() + " beans");
            User user = new User();
            user.setName("Znaji");
            user.setEmail("Znaji@Email.com");

            UserService userService = ctx.getBean(UserService.class);
            userService.createUser(user);
            System.out.println("End");
        }
    }
}

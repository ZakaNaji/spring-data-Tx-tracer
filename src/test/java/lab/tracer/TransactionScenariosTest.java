package lab.tracer;

import lab.tracer.config.AppConfig;
import lab.tracer.service.UserService;
import lab.tracer.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TransactionScenariosTest {

    @Test
    void testServices() {
        try (var ctx = new AnnotationConfigApplicationContext(AppConfig.class)) {
            var userService = ctx.getBean(UserService.class);
            var orderService = ctx.getBean(OrderService.class);

            userService.createUser(new lab.tracer.entity.User());
            orderService.createOrder(new lab.tracer.entity.Order());
        }
    }
}

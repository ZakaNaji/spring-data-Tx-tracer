package lab.tracer.service;

import lab.tracer.entity.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createOrder(Order o) {
        // TODO: persist order with EntityManager
        System.out.println("Creating order: " + o.getDescription());
    }
}

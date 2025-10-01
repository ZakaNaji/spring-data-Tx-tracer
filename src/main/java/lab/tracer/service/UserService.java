package lab.tracer.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lab.tracer.entity.User;
import org.hibernate.event.internal.EmptyEventManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void createUser(User u) {
        // TODO: persist user with EntityManager
        System.out.println("Creating user: " + u.getName());
        em.persist(u);
        System.out.println("user saved [this is before the commit]");
    }

    @Transactional(readOnly = true)
    public User findUser(Long id) {
        // TODO: retrieve user
        return null;
    }
}

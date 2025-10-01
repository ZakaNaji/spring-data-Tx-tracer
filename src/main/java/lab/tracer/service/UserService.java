package lab.tracer.service;

import lab.tracer.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Transactional
    public void createUser(User u) {
        // TODO: persist user with EntityManager
        System.out.println("Creating user: " + u.getName());
    }

    @Transactional(readOnly = true)
    public User findUser(Long id) {
        // TODO: retrieve user
        return null;
    }
}

package fr.cleanarchitecture.easyteach.authentication.application.ports;

import fr.cleanarchitecture.easyteach.authentication.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    Optional<User> findById(String id);
    List<User> findAll();
    void save(User user);
    void delete(String id);
}

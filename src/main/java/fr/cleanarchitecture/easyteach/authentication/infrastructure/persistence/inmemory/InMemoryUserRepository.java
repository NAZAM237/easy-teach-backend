package fr.cleanarchitecture.easyteach.authentication.infrastructure.persistence.inmemory;

import fr.cleanarchitecture.easyteach.authentication.application.ports.UserRepository;
import fr.cleanarchitecture.easyteach.authentication.domain.model.User;

import java.util.*;

public class InMemoryUserRepository implements UserRepository {
    private final Set<User> users = new HashSet<>();

    @Override
    public Optional<User> findByEmail(String email) {
        return users.stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }

    @Override
    public Optional<User> findById(String id) {
        return users.stream().filter(user -> user.getUserId().equals(id)).findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    @Override
    public void save(User user) {
        users.add(user);
    }

    @Override
    public void delete(String id) {
        users.removeIf(user -> user.getUserId().equals(id));
    }
}

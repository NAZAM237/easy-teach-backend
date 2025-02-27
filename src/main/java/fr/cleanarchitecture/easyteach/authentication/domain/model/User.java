package fr.cleanarchitecture.easyteach.authentication.domain.model;

import java.util.UUID;

public class User {
    private String userId;
    private String username;
    private String email;

    public User() {
        this.userId = UUID.randomUUID().toString();
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }
}

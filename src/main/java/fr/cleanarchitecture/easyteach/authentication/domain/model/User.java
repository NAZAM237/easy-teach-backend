package fr.cleanarchitecture.easyteach.authentication.domain.model;

import java.util.UUID;

public class User {
    private final String userId;
    private String userName;
    private String userBiography;
    private String userEmail;
    private String userPhone;
    private String userPhoto;
    private String userPassword;

    public User() {
        this.userId = UUID.randomUUID().toString();
    }

    public User(String userName, String biography, String email, String phone, String photo) {
        this.userId = UUID.randomUUID().toString();
        this.userName = userName;
        this.userBiography = biography;
        this.userEmail = email;
        this.userPhone = phone;
        this.userPhoto = photo;
    }

    public User(String email, String password) {
        this.userId = UUID.randomUUID().toString();
        this.userEmail = email;
        this.userPassword = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserBiography() {
        return userBiography;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserPhoto() {
        return userPhoto;
    }
}

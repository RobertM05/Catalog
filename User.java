package org.example.model;

import java.util.UUID;

public class User {
    private UUID id;
    private String username;
    private String password;  // This will be stored hashed
    private String nume;
    private String prenume;
    private String email;
    private String role;  // ADMIN, TEACHER

    public User(UUID id, String username, String password, String nume, String prenume, String email, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }
} 
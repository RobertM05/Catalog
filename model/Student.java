package org.example.model;

import java.util.UUID;

public class Student {
    private final UUID id;
    private final String nume;
    private final String prenume;
    private final String email;
    private final String grupa;

    public Student(UUID id, String nume, String prenume, String email, String grupa) {
        this.id = id;
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
        this.grupa = grupa;
    }

    public UUID getId() {
        return id;
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

    public String getGrupa() {
        return grupa;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", prenume='" + prenume + '\'' +
                ", email='" + email + '\'' +
                ", grupa='" + grupa + '\'' +
                '}';
    }
}

package org.example.model;

import java.util.UUID;

public class Student {
    private UUID id;
    private String nume;
    private String prenume;
    private String email;
    private String grupa;

    public Student(UUID id, String nume, String prenume, String email, String grupa) {
    this.id = id;
    this.nume = nume;
    this.prenume = prenume;
    this.email = email;
    this.grupa = grupa;
    }

    public UUID getId() {return id; }
    public String getNume() {return nume; }
    public String getPrenume() {return prenume; }
    public String getEmail() {return email; }
    public String getGrupa() {return grupa; }

    public void setNume(String nume) { this.nume = nume; }
    public void setPrenume(String prenume) { this.prenume = prenume; }
    public void setEmail(String email) { this.email = email; }
    public void setGrupa(String grupa) { this.grupa = grupa; }
}

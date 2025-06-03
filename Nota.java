package org.example.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Nota {
    private UUID id;
    private UUID studentId;
    private UUID disciplinaId;
    private int nota;
    private LocalDateTime dataNotarii;

    public Nota(UUID id, UUID studentId, UUID disciplinaId, int nota, LocalDateTime dataNotarii) {
        this.id = id;
        this.studentId = studentId;
        this.disciplinaId = disciplinaId;
        this.nota = nota;
        this.dataNotarii = dataNotarii;
    }

    public UUID getId() {
        return id;
    }
    public UUID getStudentId() {
        return studentId;
    }
    public UUID getDisciplinaId() {
        return disciplinaId;
    }
    public int getNota() {
        return nota;
    }
    public LocalDateTime getDataNotarii() {
        return dataNotarii;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }
    public void setDisciplinaId(UUID disciplinaId) {
        this.disciplinaId = disciplinaId;
    }
    public void setNota(int nota) {
        this.nota = nota;
    }
    public void setDataNotarii(LocalDateTime dataNotarii) {
        this.dataNotarii = dataNotarii;
    }
}

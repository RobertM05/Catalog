package org.example.model;

import java.util.UUID;
import java.time.LocalDateTime;

public class Nota {
    private UUID id;
    private UUID studentId;
    private UUID disciplinaId;
    private int nota;
    private LocalDateTime dataNotarii;

    public Nota(UUID id,UUID studentId, UUID disciplinaId, int nota, LocalDateTime dataNotarii) {
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

    public int getNota(){
        return nota;
    }

    public UUID getDisciplinaId() {
        return disciplinaId;
    }

    public LocalDateTime getDataNotarii() {
        return dataNotarii;
    }
}

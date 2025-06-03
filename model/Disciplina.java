package org.example.model;

import java.util.UUID;

public class Disciplina {
    private UUID id;
    private String nume;
    private String acronim;
    private String tipEvaluare;

    public Disciplina(UUID id, String nume, String acronim, String tipEvaluare) {
        this.id = id;
        this.nume = nume;
        this.acronim = acronim;
        this.tipEvaluare = tipEvaluare;
    }

    @Override
    public String toString() {
        return nume + " (" + acronim + ")";
    }

    public UUID getId() {
        return id;
    }

    public String getNume() {
        return nume;
    }

    public String getAcronim() {
        return acronim;
    }

    public String getTipEvaluare() {
        return tipEvaluare;
    }
}

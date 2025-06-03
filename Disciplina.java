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

    public void setId(UUID id) {
        this.id = id;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }
    public void setAcronim(String acronim) {
        this.acronim = acronim;
    }
    public void setTipEvaluare(String tipEvaluare) {
        this.tipEvaluare = tipEvaluare;
    }

}


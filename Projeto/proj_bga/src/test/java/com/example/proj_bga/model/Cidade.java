package com.example.proj_bga.model;

public class Cidade {
    private int id;
    private String nome;
    private int est_id;

    public Cidade() {}

    public Cidade(int id, String nome, int est_id) {
        this.id = id;
        this.nome = nome;
        this.est_id = est_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getEst_id() {
        return est_id;
    }

    public void setEst_id(int est_id) {
        this.est_id = est_id;
    }
}

package com.example.proj_bga.model;

public class Telefone {
    private int id;
    private String tel_numero;
    private int pes_id;

    public Telefone() {}

    public Telefone(int id, String tel_numero, int pes_id) {
        this.id = id;
        this.tel_numero = tel_numero;
        this.pes_id = pes_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTel_numero() {
        return tel_numero;
    }

    public void setTel_numero(String tel_numero) {
        this.tel_numero = tel_numero;
    }

    public int getPes_id() {
        return pes_id;
    }

    public void setPes_id(int pes_id) {
        this.pes_id = pes_id;
    }
}

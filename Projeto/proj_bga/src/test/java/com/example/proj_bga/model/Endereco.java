package com.example.proj_bga.model;

public class Endereco {
    private int id;
    private String logradouro;
    private String numero;
    private String cep;
    private String complemento;
    private String bairro;
    private int cid_id;

    public Endereco() {}

    public Endereco(int id, String logradouro, String numero, String cep,
                    String complemento, String bairro, int cid_id)
    {
        this.id = id;
        this.logradouro = logradouro;
        this.numero = numero;
        this.cep = cep;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cid_id = cid_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnd_logradouro() {
        return logradouro;
    }

    public void setEnd_logradouro(String end_logradouro) {
        this.logradouro = end_logradouro;
    }

    public String getEnd_numero() {
        return numero;
    }

    public void setEnd_numero(String end_numero) {
        this.numero = end_numero;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public int getCid_id() {
        return cid_id;
    }

    public void setCid_id(int cid_id) {
        this.cid_id = cid_id;
    }
}

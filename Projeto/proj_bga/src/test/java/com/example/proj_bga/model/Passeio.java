package com.example.proj_bga.model;

import com.example.proj_bga.dao.PasseioDAO;
import com.example.proj_bga.util.Conexao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Component
public class Passeio {
    @Autowired
    private PasseioDAO dao;

    private int id;
    private Date pas_data;
    private LocalTime pas_hora_inicio;
    private LocalTime pas_hora_final;
    private String pas_chamada_feita;
    private int pde_id;

    public Passeio(int id, Date pas_data, LocalTime pas_hora_inicio, LocalTime pas_hora_final, String pas_chamada_feita, int pde_id) {
        this.id = id;
        this.pas_data = pas_data;
        this.pas_hora_inicio = pas_hora_inicio;
        this.pas_hora_final = pas_hora_final;
        this.pas_chamada_feita = pas_chamada_feita;
        this.pde_id = pde_id;
    }

    public Passeio(Date pas_data, LocalTime pas_hora_inicio, LocalTime pas_hora_final, String pas_chamada_feita, int pde_id) {
        this.pas_data = pas_data;
        this.pas_hora_inicio = pas_hora_inicio;
        this.pas_hora_final = pas_hora_final;
        this.pas_chamada_feita = pas_chamada_feita;
        this.pde_id = pde_id;
    }

    public Passeio(){}

    public int getPasId() {
        return id;
    }

    public void setPasId(int id) {
        this.id = id;
    }

    public Date getPas_data() {
        return pas_data;
    }

    public void setPas_data(Date pas_data) {
        this.pas_data = pas_data;
    }

    public LocalTime getPas_hora_inicio() {
        return pas_hora_inicio;
    }

    public void setPas_hora_inicio(LocalTime pas_hora_inicio) {
        this.pas_hora_inicio = pas_hora_inicio;
    }

    public LocalTime getPas_hora_final() {
        return pas_hora_final;
    }

    public void setPas_hora_final(LocalTime pas_hora_final) {
        this.pas_hora_final = pas_hora_final;
    }

    public String getPas_chamada_feita() {
        return pas_chamada_feita;
    }

    public void setPas_chamada_feita(String pas_chamada_feita) {
        this.pas_chamada_feita = pas_chamada_feita;
    }

    public int getPde_id() {
        return pde_id;
    }

    public void setPde_id(int pde_id) {
        this.pde_id = pde_id;
    }

    public List<Passeio> consultarPasseio(String filtro, Conexao conexao) {
        return dao.get(filtro);
    }

    public Passeio consultarPasseioID(int id){
        return dao.get(id);
    }

    public Passeio gravarPasseio(Passeio passeio) {
        return dao.gravar(passeio);
    }

    public boolean deletarPasseio(Passeio passeio) { return dao.excluir(passeio); }

    public Passeio alterarPasseio(Passeio passeio) { return dao.alterar(passeio); }
}



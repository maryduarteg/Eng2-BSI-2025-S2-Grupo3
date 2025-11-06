package com.example.proj_bga.model;

import com.example.proj_bga.dao.PresencaOficinaDAO;
import com.example.proj_bga.util.Conexao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PresencaOficina {

    @Autowired
    private PresencaOficinaDAO dao;

    private int id;
    private int idAluno;
    private int idOficina;
    private int idDia;

    public PresencaOficina() {}

    public PresencaOficina(int id, int idAluno, int idOficina, int idDia) {
        this.id = id;
        this.idAluno = idAluno;
        this.idOficina = idOficina;
        this.idDia = idDia;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdAluno() { return idAluno; }
    public void setIdAluno(int idAluno) { this.idAluno = idAluno; }

    public int getIdOficina() { return idOficina; }
    public void setIdOficina(int idOficina) { this.idOficina = idOficina; }

    public int getIdDia() { return idDia; }
    public void setIdDia(int idDia) { this.idDia = idDia; }

    // Métodos de DAO
    public PresencaOficina gravarPresenca(PresencaOficina p, Conexao conexao) {
        return (PresencaOficina) dao.gravar(p, conexao);
    }

    public List<PresencaOficina> consultarPorOficinaEDia(int idOficina, int idDia, Conexao conexao) {
        return dao.getPorOficinaEDia(idOficina, idDia, conexao);
    }

    public Integer buscarOficinaPorDia(int idDia, Conexao conexao) {
        return dao.buscarOficinaPorDia(idDia, conexao);
    }

}

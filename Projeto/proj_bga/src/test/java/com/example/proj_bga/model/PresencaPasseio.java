package com.example.proj_bga.model;

import com.example.proj_bga.dao.PresencaPasseioDAO;
import com.example.proj_bga.util.Conexao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PresencaPasseio {

    @Autowired
    private PresencaPasseioDAO dao;

    private int id;
    private int idAluno;
    private int idDia;

    public PresencaPasseio() {}

    public PresencaPasseio(int id, int idAluno, int idDia) {
        this.id = id;
        this.idAluno = idAluno;
        this.idDia = idDia;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdAluno() { return idAluno; }
    public void setIdAluno(int idAluno) { this.idAluno = idAluno; }

    public int getIdDia() { return idDia; }
    public void setIdDia(int idDia) { this.idDia = idDia; }

    // Métodos de DAO
    public PresencaPasseio gravarPresenca(PresencaPasseio p, Conexao conexao) {
        return (PresencaPasseio) dao.gravar(p, conexao);
    }

    public List<PresencaPasseio> consultarPorDia(int idDia, Conexao conexao) {
        return dao.getPorDia(idDia, conexao);
    }
}

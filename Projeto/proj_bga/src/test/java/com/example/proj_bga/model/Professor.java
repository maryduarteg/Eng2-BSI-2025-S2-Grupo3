package com.example.proj_bga.model;

import com.example.proj_bga.dao.ProfessorDAO;
import com.example.proj_bga.util.Conexao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Professor {
    @Autowired
    private ProfessorDAO dao;
    private String matricula;
    private int pes_id;
    private int id;

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public int getPes_id() {
        return pes_id;
    }

    public void setPes_id(int pes_id) {
        this.pes_id = pes_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public List<Professor> consultar(String filtro, Conexao conexao) {
        return this.dao.get(filtro, conexao);
    }

}

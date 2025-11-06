package com.example.proj_bga.model;


import com.example.proj_bga.dao.OficinaDAO;
import com.example.proj_bga.util.Conexao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Oficina {
    @Autowired
    private OficinaDAO dao;

    private int id;
    private String descricao;
    private String status;


    public Oficina() {}

    public Oficina(int id, String descricao, String status) {
        this.id = id;
        this.descricao = descricao;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(int id) {

    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Oficina gravarOficina(Oficina novo, Conexao conexao) {
        return (Oficina) dao.gravar(novo,conexao);
    }

    public Oficina consultarOficinasID(int id, Conexao conexao) {
        return dao.get(id,conexao);

    }

    public Oficina alterarOficina(Oficina encontrada, Conexao conexao) {
        return (Oficina) dao.alterar(encontrada,conexao);
    }

    public List<Oficina> consultarOficinas(String s, Conexao conexao) {
        return dao.get("",conexao);
    }
}

package com.example.proj_bga.model;


import com.example.proj_bga.dao.OficinaDAO;
import com.example.proj_bga.util.Conexao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Oficina {
    @Autowired
    private OficinaDAO dao;

    private int id;
    private String descricao;
    private char ativo;

    public Oficina() {}

    public Oficina(int id, String descricao, char ativo) {
        this.id = id;
        this.descricao = descricao;
        this.ativo = ativo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {

    }

    public char getAtivo() {
        return ativo;
    }

    public void setAtivo(char ativo) {
        this.ativo = ativo;
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

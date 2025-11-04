package com.example.proj_bga.model;

import com.example.proj_bga.dao.PasseioDAO;
import com.example.proj_bga.util.Conexao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.List;

@Component
public class Passeio {
    @Autowired
    private PasseioDAO dao;

    private int pde_id;
    private String pde_descricao;

    public Passeio(int pde_id, String descricao) {
        this.pde_id = pde_id;
        this.pde_descricao =  descricao;
    }

    public Passeio(String descricao) {
        this.pde_descricao = descricao;
    }

    public Passeio(){}

    public int getPdeId() {
        return pde_id;
    }

    public void setPdeId(int pde_id) {
        this.pde_id = pde_id;
    }

    public String getDescricao() {
        return pde_descricao;
    }

    public void setDescricao(String descricao) {
        this.pde_descricao = descricao;
    }

    public List<Passeio> consultarPasseio(String filtro, Conexao conexao) {
        return dao.get(filtro, conexao);
    }

    public List<Passeio> consultarPasseioFiltro(String filtro, Conexao conexao) {
        return dao.get(filtro, conexao);
    }

    public Passeio consultarPasseioID(int id, Conexao conexao){
        return dao.get(id, conexao);
    }

    public Passeio gravarPasseio(Passeio passeio, Conexao conexao) {
        return dao.gravar(passeio, conexao);
    }

    public boolean deletarPasseio(Passeio passeio, Conexao conexao) {
        return dao.excluir(passeio, conexao);
    }

    public Passeio alterarPasseio(Passeio passeio, Conexao conexao) {
        return dao.alterar(passeio, conexao);
    }
}



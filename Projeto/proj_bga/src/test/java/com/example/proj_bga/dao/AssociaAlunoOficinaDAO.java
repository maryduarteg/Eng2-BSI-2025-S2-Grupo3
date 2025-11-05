package com.example.proj_bga.dao;

import com.example.proj_bga.model.AssociaAlunoOficina;
import com.example.proj_bga.util.Conexao;

import java.util.List;

public class AssociaAlunoOficinaDAO implements IDAO<AssociaAlunoOficina> {

    @Override
    public AssociaAlunoOficina gravar(AssociaAlunoOficina associaAlunoOficina, Conexao conexao) {
        return null;
    }

    @Override
    public AssociaAlunoOficina alterar(AssociaAlunoOficina associaAlunoOficina, Conexao conexao) {
        return null;
    }

    @Override
    public boolean excluir(AssociaAlunoOficina associaAlunoOficina, Conexao conexao) {
        return false;
    }

    @Override
    public List<AssociaAlunoOficina> get(String filtro, Conexao conexao) {
        return null;
    }

    @Override
    public AssociaAlunoOficina get(int id,  Conexao conexao) {
        return null;
    }
}

package com.example.proj_bga.dao;

import com.example.proj_bga.model.Aluno;
import com.example.proj_bga.model.Oficina;
import com.example.proj_bga.util.SingletonDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO implements IDAO<Aluno>
{

    @Override
    public Object gravar(Aluno entidade) {
        return null;
    }

    @Override
    public Object alterar(Aluno entidade) {
        return null;
    }

    @Override
    public boolean excluir(Aluno entidade) {
        return false;
    }

    @Override
    public List<Aluno> get(String filtro) {
        return List.of();
    }

    @Override
    public Aluno get(int id) {return null;}
}

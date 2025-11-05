package com.example.proj_bga.dao;

import com.example.proj_bga.model.Aluno;
import com.example.proj_bga.model.Professor;
import com.example.proj_bga.util.Conexao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDAO implements IDAO<Professor>{
    @Override
    public Object gravar(Professor entidade, Conexao conexao) {
        return null;
    }

    @Override
    public Object alterar(Professor entidade, Conexao conexao) {
        return null;
    }

    @Override
    public boolean excluir(Professor entidade, Conexao conexao) {
        return false;
    }

    @Override
    public List<Professor> get(String filtro, Conexao conexao) {
        List<Professor> lista = new ArrayList<>();
        String sql = "Select * from aluno ";
        if(!filtro.isEmpty())
            sql = sql + filtro;

        try {
            ResultSet rs = conexao.consultar(sql);
            while (rs.next()) {
                Professor prof = new Professor();
                prof.setId(rs.getInt("prof_id"));
                prof.setMatricula(rs.getString("prof_matricula"));
                prof.setPes_id(rs.getInt("pes_id"));
                lista.add(prof);
            }
            return lista;
        } catch (SQLException e) {
            System.out.println("Erro ao buscar professores: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Professor get(int id, Conexao conexao) {
        return null;
    }
}

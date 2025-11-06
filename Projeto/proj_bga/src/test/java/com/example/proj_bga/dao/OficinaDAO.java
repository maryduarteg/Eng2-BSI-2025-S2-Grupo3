package com.example.proj_bga.dao;

import com.example.proj_bga.model.Aluno;
import com.example.proj_bga.model.Oficina;
import com.example.proj_bga.util.Conexao;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Repository
public class OficinaDAO implements IDAO<Oficina>{
    @Override
    public Object gravar(Oficina entidade, Conexao conexao) {
        String sql = """
                INSERT INTO oficina(
                	ofc_descricao, ofc_ativo)
                	VALUES ('#1', 'S');
                """;
        sql = sql.replace("#1", entidade.getDescricao());


        try {
            if(conexao.manipular(sql))
                return entidade;
        } catch (Exception e) {
            System.out.println("Erro ao gravar Oficina: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Object alterar(Oficina entidade, Conexao conexao)
    {
        Oficina retorno = null;
        String sql = """
             UPDATE oficina
             SET ofc_descricao='#1', ofc_ativo = '#2'
             WHERE ofc_id=#3;
             """;

        sql = sql.replace("#1",entidade.getDescricao());
        sql = sql.replace("#2",""+entidade.getAtivo());
        sql = sql.replace("#3", ""+entidade.getId());


        try {
            if(conexao.manipular(sql))
                retorno = entidade;
        } catch (Exception e) {
            System.out.println("Erro ao alterar Aluno: " + e.getMessage());
        }
        return retorno;
    }

    @Override
    public boolean excluir(Oficina entidade, Conexao conexao) {
        return false;
    }

    @Override
    public List<Oficina> get(String filtro, Conexao conexao) {
        List<Oficina> lista = new ArrayList<>();
        String sql = "Select * from oficina ";
        if(!filtro.isEmpty())
            sql = sql + filtro;

        try {
            ResultSet rs = conexao.consultar(sql);
            while (rs.next()) {
                Oficina a = new Oficina();
                a.setId(rs.getInt("ofc_id"));
                a.setDescricao(rs.getString("ofc_descricao"));
                a.setAtivo(rs.getString("ofc_ativo").charAt(0));
                lista.add(a);
            }
            return lista;
        } catch (SQLException e) {
            System.out.println("Erro ao buscar aluno: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Oficina get(int id, Conexao conexao) {
        Oficina a = null;
        String sql = """
                SELECT * FROM oficina
                WHERE ofc_id = #1;
            """;

        sql = sql.replace("#1", "" + id);

        try {
            ResultSet rs = conexao.consultar(sql);
            if(rs.next()){
                a = new Oficina();
                a.setId(rs.getInt("ofc_id"));
                a.setDescricao(rs.getString("ofc_descricao"));
                a.setAtivo(rs.getString("ofc_ativo").charAt(0));
            }
        } catch(Exception e) {
            System.out.println("Erro ao obter Oficina: " + e.getMessage());
        }
        return a;
    }
}
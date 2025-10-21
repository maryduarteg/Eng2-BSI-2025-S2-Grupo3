package com.example.proj_bga.dao;

import com.example.proj_bga.model.Oficina;
import com.example.proj_bga.util.SingletonDB;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OficinaDAO implements IDAO<Oficina>{

    @Override
    public Oficina gravar(Oficina oficina){
        String sql = """
                        INSERT INTO oficina (
                            ofc_nome, ofc_hora_inicio, ofc_hora_termino, ofc_dt_inicial, ofc_dt_final, prof_id, ofc_ativo)
                            VALUES (#1, #2, #3, #4, #5, #6, #7);
                   """;

        sql = sql.replace("#1", oficina.getNome());
        sql = sql.replace("#2", oficina.getHoraInicio().toString());
        sql = sql.replace("#3", oficina.getHoraTermino().toString());
        sql = sql.replace("#4", oficina.getDataInicio().toString());
        sql = sql.replace("#5", oficina.getDataFim().toString());
        sql = sql.replace("#6", "" + oficina.getProfessor());
        sql = sql.replace("#7", "" + oficina.getAtivo());

        ResultSet resultado = SingletonDB.getConexao().consultar(sql);
        try {
            if(resultado.next()){
                oficina.setId(resultado.getInt("ofc_id"));
                return oficina;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao gravar Oficina: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Oficina alterar(Oficina oficina){
        String sql = """
                        UPDATE oficina
                            SET , ofc_nome = #1, ofc_hora_inicio = #2, ofc_hora_termino = #3, ofc_dt_inicial = #4, ofc_dt_final = #5, prof_id = #6, ofc_ativo = #7
                            WHERE ofc_id = #8;
                    """;

        sql = sql.replace("#1", oficina.getNome());
        sql = sql.replace("#2", oficina.getHoraInicio().toString());
        sql = sql.replace("#3", oficina.getHoraTermino().toString());
        sql = sql.replace("#4", oficina.getDataInicio().toString());
        sql = sql.replace("#5", oficina.getDataFim().toString());
        sql = sql.replace("#6", "" + oficina.getProfessor());
        sql = sql.replace("#7", "" + oficina.getAtivo());
        sql = sql.replace("#8", "" + oficina.getId());

        if(SingletonDB.getConexao().manipular(sql))
            return oficina;
        else{
            System.out.println("Erro ao alterar Oficina: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public boolean excluir(Oficina oficina)
    {
        if(oficina == null)
            return false;

        String sql = """
                        DELETE FROM oficina
                            WHERE ofc_id = #1;
                    """;
        sql = sql.replace("#1", "" + oficina.getId());
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public List<Oficina> get(String filtro){
        List<Oficina> oficinas = new ArrayList<>();
        String sql = """
                        SELECT * FROM oficina
                     """;
        if(!filtro.isEmpty()){
            sql = sql.replace("#1", filtro);
        }

        ResultSet resultado = SingletonDB.getConexao().consultar(sql);
        try{
            while(resultado.next()){
                Oficina oficina = new Oficina(
                        resultado.getInt("ofc_cod"),
                        resultado.getString("ofc_desc")
                );
            }
        } catch(SQLException e){
            System.out.println("Erro ao listar Oficinas: " + e.getMessage());
        }
        return oficinas;
    }

    @Override
    public Oficina get(int id){
        Oficina oficina = null;
        String sql = """
                        SELECT * FROM oficina
                     """;
        if(id > 0){
            sql = sql.replace("#1", "" + id);
        }

        ResultSet resultado = SingletonDB.getConexao().consultar(sql);
        try{
            while(resultado.next()){
                oficina = new Oficina(
                        resultado.getInt("ofc_cod"),
                        resultado.getString("ofc_desc")
                );
            }
        } catch(SQLException e){
            System.out.println("Erro ao listar Oficinas: " + e.getMessage());
        }
        return oficina;
    }
}

package com.example.proj_bga.dao;

import com.example.proj_bga.model.AssociaAlunoOficina;
import com.example.proj_bga.util.Conexao;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
@Repository
public class AssociaAlunoOficinaDAO implements IDAO<AssociaAlunoOficina> {

    @Override
    public AssociaAlunoOficina gravar(AssociaAlunoOficina associaAlunoOficina, Conexao conexao) {
        String sql = """
                    INSERT INTO r_aluno_oficina(alu_id,ofc_id)
                        VALUES(#1, #2);
                """;

        sql = sql.replace("#1", "" +  associaAlunoOficina.getAlu_id() );
        sql = sql.replace("#2", "" +  associaAlunoOficina.getOfc_id() );
        try{
            if(conexao.manipular(sql)){
                return associaAlunoOficina;
            }
            else{
                System.out.println("Não foi possível associar o aluno à oficina!");
            }
        }
        catch(Exception e){
            System.out.println("Erro: " + e.getMessage());
        }

        return null;
    }

    @Override
    public AssociaAlunoOficina alterar(AssociaAlunoOficina associaAlunoOficina, Conexao conexao) {
        return null;
    }

    @Override
    public boolean excluir(AssociaAlunoOficina associaAlunoOficina, Conexao conexao) {
        if(associaAlunoOficina == null){
            return false;
        }

        String sql = """
                    DELETE FROM r_aluno_oficina
                        WHERE alu_id = #1 AND ofc_id = #2;
                """;

        sql = sql.replace("#1", "" +  associaAlunoOficina.getAlu_id() );
        sql = sql.replace("#2", "" +  associaAlunoOficina.getOfc_id() );

        try {
            if(conexao.manipular(sql))
                return true;
        } catch (Exception e) {
            System.out.println("Erro ao excluir o registro: " + e.getMessage());
        }

        return false;
    }

    @Override
    public List<AssociaAlunoOficina> get(String filtro, Conexao conexao) {
        List<AssociaAlunoOficina> lista = new ArrayList<>();
        String sql = """
                    SELECT * FROM r_aluno_oficina
                """;

        if(!filtro.isEmpty()){
            sql += "WHERE alu_id LIKE '%" + filtro + "%'";
        }

        ResultSet resultado = conexao.consultar(sql);

        try{
            if (resultado != null) {
                while (resultado.next()) {
                    AssociaAlunoOficina associaAlunoOficina = new AssociaAlunoOficina(
                            resultado.getInt("alu_id"),
                            resultado.getInt("ofc_id")
                    );

                    lista.add(associaAlunoOficina);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao alunos: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public AssociaAlunoOficina get(int idAlu,  Conexao conexao) {
        if(idAlu <= 0)
            return null;

        AssociaAlunoOficina associaAlunoOficina = null;
        String sql = """
                    SELECT * FROM r_aluno_oficina
                        WHERE alu_id = #1;
                """;
        sql = sql.replace("#1", "" +  idAlu);

        ResultSet resultado = conexao.consultar(sql);
        try{
            if(resultado != null && resultado.next()){
                associaAlunoOficina = new AssociaAlunoOficina(
                        resultado.getInt("alu_id"),
                        resultado.getInt("ofc_id")
                );
                return associaAlunoOficina;
            }
        }
        catch(Exception e){
            System.out.println("Erro ao consultar dados: " + e.getMessage());
        }

        return null;
    }
}
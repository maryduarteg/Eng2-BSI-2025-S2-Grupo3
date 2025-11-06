package com.example.proj_bga.dao;

import com.example.proj_bga.model.AssociaAlunoOficina;
import com.example.proj_bga.util.Conexao;

import java.sql.ResultSet;
import java.util.List;

public class AssociaAlunoOficinaDAO implements IDAO<AssociaAlunoOficina> {

    @Override
    public AssociaAlunoOficina gravar(AssociaAlunoOficina associaAlunoOficina, Conexao conexao) {
        String sql = """
                    INSERT INTO r_aluno_oficina(alu_id,ofc_id)
                        VALUES('#1', '#2');
                """;

        sql = sql.replace("#1", "" +  associaAlunoOficina.getAlu_id() );
        sql = sql.replace("#2", "" +  associaAlunoOficina.getOfc_id() );

        ResultSet resultado = conexao.consultar(sql);

        try{
            if(resultado != null && resultado.next()){
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
                        WHERE alu_id = #1;
                """;

        sql = sql.replace("#1", "" +  associaAlunoOficina.getAlu_id() );

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
        return null;
    }

    @Override
    public AssociaAlunoOficina get(int id,  Conexao conexao) {
        return null;
    }
}

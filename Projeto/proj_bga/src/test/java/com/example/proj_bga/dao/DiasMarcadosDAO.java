package com.example.proj_bga.dao;

import com.example.proj_bga.model.DiasMarcados;
import com.example.proj_bga.util.Conexao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DiasMarcadosDAO implements IDAO<DiasMarcados>{
    @Override
    public Object gravar(DiasMarcados entidade, Conexao conexao) {
        String sql = """
                INSERT INTO .dias_marcados_oficinas(dmf_dia, ofc_id)
                VALUES (#1, #2);
                """;
        sql = sql.replace("#1",""+ entidade.getData());
        sql = sql.replace("#2", ""+entidade.getIdOFc());

        try {
            if(conexao.manipular(sql))
                return entidade;
        } catch (Exception e) {
            System.out.println("Erro ao gravar Dias Marcados: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Object alterar(DiasMarcados entidade, Conexao conexao) {
        DiasMarcados retorno = null;
        String sql = """
                UPDATE public.dias_marcados_oficinas
                	SET dmf_dia=#1, ofc_id=#2
                	WHERE dmf_id = #3;
             """;

        sql = sql.replace("#1",""+entidade.getData());
        sql = sql.replace("#2",""+entidade.getIdOFc());
        sql = sql.replace("#3", ""+entidade.getId());

        try {
            if(conexao.manipular(sql))
                retorno = entidade;
        } catch (Exception e) {
            System.out.println("Erro ao alterar dias marcados: " + e.getMessage());
        }
        return retorno;
    }

    @Override
    public boolean excluir(DiasMarcados entidade, Conexao conexao) {
        String sql = """
                DELETE FROM public.dias_marcados_oficinas
                WHERE dmf_id = #1;""";
        sql = sql.replace("#1",""+ entidade.getId());
        try {
            if(conexao.manipular(sql))
                return true;
        } catch (Exception e) {
            System.out.println("Erro ao excluir dias marcados: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<DiasMarcados> get(String filtro, Conexao conexao) {
        List<DiasMarcados> lista = new ArrayList<>();
        String sql = "Select * from dias_marcados_oficinas ";
        if(!filtro.isEmpty())
            sql = sql + filtro;

        try {
            ResultSet rs = conexao.consultar(sql);
            while (rs.next()) {
                DiasMarcados dia = new DiasMarcados();
                dia.setId(rs.getInt("dmf_id"));
                dia.setData(rs.getDate("dmf_dia").toLocalDate());
                dia.setIdOFc(rs.getInt("ofc_id"));
                lista.add(dia);
            }
            return lista;
        } catch (SQLException e) {
            System.out.println("Erro ao buscar dias marcados: " + e.getMessage());
        }
        return null;
    }

    @Override
    public DiasMarcados get(int id, Conexao conexao) {
        String sql = "Select * from dias_marcados_oficinas where dmf_id= #1 ";
        sql = sql.replace("#1", ""+id);

        try {
            ResultSet rs = conexao.consultar(sql);
            DiasMarcados dia = new DiasMarcados();
            dia.setId(rs.getInt("dmf_id"));
            dia.setData(rs.getDate("dmf_dia").toLocalDate());
            dia.setIdOFc(rs.getInt("ofc_id"));

            return dia;
        } catch (SQLException e) {
            System.out.println("Erro ao buscar dias marcados: " + e.getMessage());
        }
        return null;
    }
}

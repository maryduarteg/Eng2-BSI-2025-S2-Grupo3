package com.example.proj_bga.dao;

import com.example.proj_bga.model.PresencaOficina;
import com.example.proj_bga.util.Conexao;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PresencaOficinaDAO implements IDAO<PresencaOficina> {

    @Override
    public Object gravar(PresencaOficina entidade, Conexao conexao) {
        String sql = String.format("""
                INSERT INTO presenca_oficina (alu_id, ofc_id, dmf_id)
                VALUES (%d, %d, %d)
                """, entidade.getIdAluno(), entidade.getIdOficina(), entidade.getIdDia());

        try {
            if (conexao.manipular(sql)) return entidade;
        } catch (Exception e) {
            System.out.println("Erro ao gravar PresencaOficina: " + e.getMessage());
        }
        return null;
    }

    public List<PresencaOficina> getPorOficinaEDia(int idOficina, int idDia, Conexao conexao) {
        List<PresencaOficina> lista = new ArrayList<>();
        String sql = String.format("""
                SELECT * FROM presenca_oficina
                WHERE ofc_id = %d AND dmf_id = %d
                """, idOficina, idDia);

        try {
            ResultSet rs = conexao.consultar(sql);
            while (rs.next()) {
                PresencaOficina p = new PresencaOficina();
                p.setId(rs.getInt("pso_id"));
                p.setIdAluno(rs.getInt("alu_id"));
                p.setIdOficina(rs.getInt("ofc_id"));
                p.setIdDia(rs.getInt("dmf_id"));
                lista.add(p);
            }
            return lista;
        } catch (Exception e) {
            System.out.println("Erro ao buscar PresencaOficina: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Object alterar(PresencaOficina entidade, Conexao conexao) {
        return null; // não será usado
    }

    @Override
    public boolean excluir(PresencaOficina entidade, Conexao conexao) {
        return false;
    }

    @Override
    public List<PresencaOficina> get(String filtro, Conexao conexao) {
        return null;
    }

    @Override
    public PresencaOficina get(int id, Conexao conexao) {
        return null;
    }
}

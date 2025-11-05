package com.example.proj_bga.dao;

import com.example.proj_bga.model.DiasOficina;
import com.example.proj_bga.model.Pessoa;
import com.example.proj_bga.util.Conexao;

import java.sql.ResultSet;
import java.util.List;

public class DiasOficinaDAO implements IDAO<DiasOficina>{
    @Override
    public Object gravar(DiasOficina entidade, Conexao conexao) {
        return null;
    }

    @Override
    public Object alterar(DiasOficina entidade, Conexao conexao) {
        return null;
    }

    @Override
    public boolean excluir(DiasOficina entidade, Conexao conexao) {
        return false;
    }

    @Override
    public List<DiasOficina> get(String filtro, Conexao conexao) {
        DiasOficina p = null;
        /*String sql = """
                SELECT * FROM pessoa
                WHERE pes_id = #1;
            """;

        sql = sql.replace("#1", ""+id);
        ResultSet rs = conexao.consultar(sql);

        try {
            if(rs.next()){
                p = new Pessoa();
                p.setId(rs.getInt("pes_id"));
                p.setNome(rs.getString("pes_nome"));
                p.setcpf(rs.getString("pes_cpf"));
                p.setDt_nascimento(rs.getDate("pes_dt_nascimento").toLocalDate());
                p.setRg(rs.getString("pes_rg"));
                p.setAtivo(rs.getString("pes_ativo").charAt(0));
                p.setEnd_id(rs.getInt("end_id"));
            }
        } catch(Exception e) {
            System.out.println("Erro ao obter Dias Oficina: " + e.getMessage());
        }*/
        return null;
    }

    @Override
    public DiasOficina get(int id, Conexao conexao) {
        return null;
    }
}

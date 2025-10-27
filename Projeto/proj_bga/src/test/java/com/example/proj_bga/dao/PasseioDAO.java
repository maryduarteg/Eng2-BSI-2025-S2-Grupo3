package com.example.proj_bga.dao;

import com.example.proj_bga.model.Passeio;
import com.example.proj_bga.util.SingletonDB;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PasseioDAO implements IDAO<Passeio> {

    @Override
    public Passeio gravar(Passeio passeio) {
        String sql = """
                INSERT INTO passeio_descricao(pde_descricao)
                    VALUES ('#1') RETURNING pde_id; 
            """;

        sql = sql.replace("#1", passeio.getDescricao());

        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            if (rs != null && rs.next()) {
                passeio.setPdeId(rs.getInt("pde_id"));
                return passeio;
            }
        } catch(SQLException e) {
            System.out.println("Erro ao obter ID do Passeio: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Passeio alterar(Passeio passeio) {

        String sql = """
                    UPDATE passeio_descricao SET pde_descricao = '#1'
                        WHERE pde_id = #2;
                """;
        sql = sql.replace("#1", passeio.getDescricao());
        sql = sql.replace("#2", "" + passeio.getPdeId());

        if(SingletonDB.getConexao().manipular(sql)){
            return passeio;
        }
        else{
            System.out.println("Erro ao alterar Passeio:" + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public boolean excluir(Passeio passeio) {
        if (passeio == null)
            return false;

        String sql = """
                    DELETE FROM passeio_descricao
                        WHERE pde_id = #1;
                 """;

        sql = sql.replace("#1", "" + passeio.getPdeId());

        System.out.println("SQL de Exclusão: " + sql);
        if (SingletonDB.getConexao().manipular(sql)) {
            return true;
        } else {
            System.err.println("Erro SQL no DELETE: " + SingletonDB.getConexao().getMensagemErro());
            return false;
        }
    }

    @Override
    public Passeio get(int pde_id){
        Passeio p = null;
        String sql = """
                SELECT * FROM passeio_descricao
                    WHERE pde_id = #1;
            """;

        sql = sql.replace("#1", "" + pde_id);

        try {
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            if(rs.next()){
                p = new Passeio(
                        rs.getInt("pde_id"),
                        rs.getString("pde_descricao")
                );

            }
        } catch(Exception e) {
            System.out.println("Erro ao obter Passeio: " + e.getMessage());
        }
        return p;
    }

    @Override
    public List<Passeio> get(String filtro){
        List<Passeio> lista = new ArrayList<>();
        String sql = "SELECT * FROM passeio_descricao";

        if(!filtro.isEmpty()){
            String termoBusca = "'%" + filtro + "%'";
            sql += " WHERE pde_descricao ILIKE " + termoBusca;
        }
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try{
            while(rs.next()){
                Passeio passeio = new Passeio(
                        rs.getInt("pde_id"),
                        rs.getString("pde_descricao")
                );
                lista.add(passeio);
            }
        } catch (Exception e){
            System.out.println("Erro ao consultar Passeio:" + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

}

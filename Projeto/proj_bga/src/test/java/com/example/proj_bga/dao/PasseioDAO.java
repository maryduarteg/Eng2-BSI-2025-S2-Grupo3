package com.example.proj_bga.dao;

import com.example.proj_bga.model.Passeio;
import com.example.proj_bga.util.SingletonDB;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PasseioDAO implements IDAO<Passeio> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    public Passeio gravar(Passeio passeio) {
        String dataFormatada = DATE_FORMAT.format(passeio.getPas_data());

        String horaInicio = passeio.getPas_hora_inicio().toString();
        String horaFinal = passeio.getPas_hora_final().toString();

        String sql = """
                    INSERT INTO passeio(pas_data, pas_hora_inicio, pas_hora_final, pas_chamada_feita, pde_id)
                        VALUES ('#1', '#2', '#3', '#4', #5) RETURNING pas_id;
                """;

        sql = sql.replace("#1", dataFormatada);
        sql = sql.replace("#2", horaInicio);
        sql = sql.replace("#3", horaFinal);
        sql = sql.replace("#4", passeio.getPas_chamada_feita());
        sql = sql.replace("#5", "" + passeio.getPde_id());

        ResultSet rs = SingletonDB.getConexao().consultar(sql);

        try {
            if (rs != null && rs.next()) {
                passeio.setPasId(rs.getInt("pas_id"));
                return passeio;
            }
        } catch(SQLException e) {
            System.out.println("Erro ao obter ID do Passeio: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Passeio alterar(Passeio passeio) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String sql = """
                    UPDATE passeio SET pas_data = '#1', pas_hora_inicio = '#2', pas_hora_final = '#3', pas_chamada_feita = '#4', pde_id = #5
                        WHERE pas_id = #6;
                """;
        sql = sql.replace("#1", dateFormatter.format(passeio.getPas_data()));
        sql = sql.replace("#2", passeio.getPas_hora_inicio().toString());
        sql = sql.replace("#3", passeio.getPas_hora_final().toString());
        sql = sql.replace("#4", passeio.getPas_chamada_feita());
        sql = sql.replace("#5", "" + passeio.getPde_id());
        sql = sql.replace("#6", "" + passeio.getPasId());

        System.out.println("SQL FINAL DE UPDATE: " + sql);

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
                    DELETE FROM passeio
                    WHERE pas_id = #1;
                 """;

        sql = sql.replace("#1", "" + passeio.getPasId());

        System.out.println("SQL de Exclusão: " + sql);
        if (SingletonDB.getConexao().manipular(sql)) {
            return true;
        } else {
            System.err.println("Erro SQL no DELETE: " + SingletonDB.getConexao().getMensagemErro());
            return false;
        }
    }

    @Override
    public Passeio get(int id){
        Passeio p = null;
        String sql = """
                SELECT * FROM passeio
                WHERE pas_id = #1;
            """;

        sql = sql.replace("#1", "" + id);

        try {
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            if(rs.next()){
                p = new Passeio(
                        rs.getInt("pas_id"),
                        rs.getDate("pas_data"),
                        rs.getObject("pas_hora_inicio", LocalTime.class),
                        rs.getObject("pas_hora_final", LocalTime.class),
                        rs.getString("pas_chamada_feita"),
                        rs.getInt("pde_id")
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
        String sql = """
                    SELECT * FROM passeio;
                """;

        if(!filtro.isEmpty()){
            sql += "WHERE " + filtro;
        }
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try{
            while(rs.next()){
                Passeio passeio = new Passeio(
                        rs.getInt("pas_id"),
                        rs.getDate("pas_data"),
                        rs.getObject("pas_hora_inicio", LocalTime.class),
                        rs.getObject("pas_hora_final", LocalTime.class),
                        rs.getString("pas_chamada_feita"),
                        rs.getInt("pde_id")
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

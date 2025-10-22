package com.example.proj_bga.dao;

import com.example.proj_bga.model.Oficina;
import com.example.proj_bga.util.SingletonDB;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OficinaDAO implements IDAO<Oficina>{

    @Override
    public Oficina gravar(Oficina oficina) {
        // SQL com RETURNING para pegar o ID gerado
        String sql = String.format("""
        INSERT INTO oficina (
            ofc_nome, ofc_hora_inicio, ofc_hora_termino, ofc_dt_inicial, ofc_dt_final, prof_id, ofc_ativo
        ) VALUES ('%s', '%s', '%s', '%s', '%s', %d, '%s')
        RETURNING ofc_id
    """,
                oficina.getNome(),
                oficina.getHoraInicio().toString(),
                oficina.getHoraTermino().toString(),
                new java.sql.Date(oficina.getDataInicio().getTime()),
                new java.sql.Date(oficina.getDataFim().getTime()),
                oficina.getProfessor(),
                oficina.getAtivo()
        );

        // Executa a consulta
        ResultSet resultado = SingletonDB.getConexao().consultar(sql);

        try {
            if (resultado != null && resultado.next()) {
                // Recupera o ID gerado pelo banco
                oficina.setId(resultado.getInt("ofc_id"));
                return oficina;
            } else {
                System.out.println("Erro: ResultSet nulo ou nenhum registro retornado.");
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
                SET ofc_nome = '#1',
                    ofc_hora_inicio = '#2',
                    ofc_hora_termino = '#3',
                    ofc_dt_inicial = '#4',
                    ofc_dt_final = '#5',
                    prof_id = #6,
                    ofc_ativo = '#7'
                WHERE ofc_id = #8;
            """;

        sql = sql.replace("#1", oficina.getNome());
        sql = sql.replace("#2", oficina.getHoraInicio().toString());
        sql = sql.replace("#3", oficina.getHoraTermino().toString());
        sql = sql.replace("#4", new java.sql.Date(oficina.getDataInicio().getTime()).toString());
        sql = sql.replace("#5", new java.sql.Date(oficina.getDataFim().getTime()).toString());
        sql = sql.replace("#6", "" + oficina.getProfessor());
        sql = sql.replace("#7", "" + oficina.getAtivo());
        sql = sql.replace("#8", "" + oficina.getId());

        if(SingletonDB.getConexao().manipular(sql))
            return oficina;
        else {
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

    // Retorna lista de Oficinas (GET)
    public List<Oficina> get(String filtro) {
        List<Oficina> oficinas = new ArrayList<>();
        String sql = "SELECT * FROM oficina";

        // Se quiser filtro futuro, pode adicionar WHERE aqui
        if (filtro != null && !filtro.isEmpty()) {
            sql += " WHERE " + filtro;
        }

        ResultSet resultado = SingletonDB.getConexao().consultar(sql);
        try {
            while (resultado.next()) {
                Oficina oficina = new Oficina(
                        resultado.getInt("ofc_id"),     // id correto
                        resultado.getString("ofc_nome") // nome correto
                );

                // Preenchendo os outros campos, se quiser que getOficina tenha tudo
                oficina.setHoraInicio(resultado.getTime("ofc_hora_inicio").toLocalTime());
                oficina.setHoraTermino(resultado.getTime("ofc_hora_termino").toLocalTime());
                oficina.setDataInicio(resultado.getDate("ofc_dt_inicial"));
                oficina.setDataFim(resultado.getDate("ofc_dt_final"));
                oficina.setProfessor(resultado.getInt("prof_id"));
                oficina.setAtivo(resultado.getString("ofc_ativo").charAt(0));

                oficinas.add(oficina); // ESSENCIAL: adiciona na lista
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar Oficinas: " + e.getMessage());
        }

        return oficinas;
    }

    // Retorna uma Oficina pelo ID
    public Oficina getID(int id) {
        String sql = "SELECT * FROM oficina WHERE ofc_id = " + id;
        ResultSet resultado = SingletonDB.getConexao().consultar(sql);

        try {
            if (resultado.next()) {
                Oficina oficina = new Oficina(
                        resultado.getInt("ofc_id"),
                        resultado.getString("ofc_nome")
                );

                oficina.setNome(resultado.getString("ofc_nome"));
                oficina.setHoraInicio(resultado.getTime("ofc_hora_inicio").toLocalTime());
                oficina.setHoraTermino(resultado.getTime("ofc_hora_termino").toLocalTime());
                oficina.setDataInicio(resultado.getDate("ofc_dt_inicial"));
                oficina.setDataFim(resultado.getDate("ofc_dt_final"));
                oficina.setProfessor(resultado.getInt("prof_id"));
                oficina.setAtivo(resultado.getString("ofc_ativo").charAt(0));

                return oficina;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar Oficina por ID: " + e.getMessage());
        }

        return null;
    }

    @Override
    public Oficina get(int id){
        Oficina oficina = null;
        if(id <= 0) return null;

        String sql = "SELECT * FROM oficina WHERE ofc_id = " + id;

        ResultSet resultado = SingletonDB.getConexao().consultar(sql);
        try {
            if(resultado.next()){
                oficina = new Oficina(
                        resultado.getInt("ofc_id"),      // coluna correta no DB
                        resultado.getString("ofc_nome")  // coluna correta no DB
                );
            }
        } catch(SQLException e){
            System.out.println("Erro ao buscar Oficina: " + e.getMessage());
        }
        return oficina;
    }

}

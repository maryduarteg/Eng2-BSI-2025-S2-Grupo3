package com.example.proj_bga.dao;

import com.example.proj_bga.model.Oficina;
import com.example.proj_bga.util.Conexao;
import com.example.proj_bga.util.SingletonDB;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.*;

@Repository
public class OficinaDAO implements IDAO<Oficina> {

    @Override
    public Oficina gravar(Oficina oficina, Conexao conexao) {
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

        ResultSet resultado = conexao.consultar(sql);

        try {
            if (resultado != null && resultado.next()) {
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
    public Oficina alterar(Oficina oficina, Conexao conexao) {
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
        sql = sql.replace("#6", String.valueOf(oficina.getProfessor()));
        sql = sql.replace("#7", String.valueOf(oficina.getAtivo()));
        sql = sql.replace("#8", String.valueOf(oficina.getId()));

        if (conexao.manipular(sql)) {
            return oficina;
        } else {
            System.out.println("Erro ao alterar Oficina: " + conexao.getMensagemErro());
            return null;
        }
    }

    @Override
    public boolean excluir(Oficina oficina, Conexao conexao) {
        if (oficina == null) return false;
        return inativarOficina(oficina.getId(), conexao);
    }

    public boolean inativarOficina(int id, Conexao conexao) {
        String sql = """
                UPDATE oficina
                SET ofc_ativo = 'I'
                WHERE ofc_id = #1;
                """;
        sql = sql.replace("#1", String.valueOf(id));

        if (conexao.manipular(sql)) {
            return true;
        } else {
            System.err.println("Erro ao inativar Oficina: " + conexao.getMensagemErro());
            return false;
        }
    }

    @Override
    public Oficina get(int id, Conexao conexao) {
        if (id <= 0) return null;

        String sql = "SELECT * FROM oficina WHERE ofc_id = " + id;
        ResultSet resultado = conexao.consultar(sql);

        try {
            if (resultado.next()) {
                Oficina oficina = new Oficina(
                        resultado.getInt("ofc_id"),
                        resultado.getString("ofc_nome")
                );

                oficina.setHoraInicio(resultado.getTime("ofc_hora_inicio").toLocalTime());
                oficina.setHoraTermino(resultado.getTime("ofc_hora_termino").toLocalTime());
                oficina.setDataInicio(resultado.getDate("ofc_dt_inicial"));
                oficina.setDataFim(resultado.getDate("ofc_dt_final"));
                oficina.setProfessor(resultado.getInt("prof_id"));
                oficina.setAtivo(resultado.getString("ofc_ativo").charAt(0));

                return oficina;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar Oficina: " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Oficina> get(String filtro, Conexao conexao) {
        List<Oficina> oficinas = new ArrayList<>();
        String sql = "SELECT * FROM oficina";

        if (filtro != null && !filtro.isEmpty()) {
            sql += " WHERE " + filtro;
        }

        ResultSet resultado = conexao.consultar(sql);

        try {
            while (resultado.next()) {
                Oficina oficina = new Oficina(
                        resultado.getInt("ofc_id"),
                        resultado.getString("ofc_nome")
                );

                oficina.setHoraInicio(resultado.getTime("ofc_hora_inicio").toLocalTime());
                oficina.setHoraTermino(resultado.getTime("ofc_hora_termino").toLocalTime());
                oficina.setDataInicio(resultado.getDate("ofc_dt_inicial"));
                oficina.setDataFim(resultado.getDate("ofc_dt_final"));
                oficina.setProfessor(resultado.getInt("prof_id"));
                oficina.setAtivo(resultado.getString("ofc_ativo").charAt(0));

                oficinas.add(oficina);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar Oficinas: " + e.getMessage());
        }

        return oficinas;
    }

    // Retorna uma Oficina pelo ID, sem precisar usar o get(int)
    public Oficina getID(int id, Conexao conexao) {
        return get(id, conexao);
    }

    public boolean existeConflitoDeHorario(int professorId, Date dataInicio, Date dataFim, LocalTime horaInicio, LocalTime horaFim, Conexao conexao) {
        String sql = """ 
                SELECT ofc_hora_inicio, 
                ofc_hora_termino, ofc_dt_inicial,
                 ofc_dt_final FROM oficina WHERE prof_id = %d 
                 AND ofc_ativo = 'S' 
                 AND (
                  (ofc_dt_inicial <= '%s' 
                  AND ofc_dt_final >= '%s') 
                  ) 
                  """.
                formatted( professorId, new java.sql.Date(dataFim.getTime()), new java.sql.Date(dataInicio.getTime()) );
        ResultSet rs = conexao.consultar(sql);
        try {
            while (rs != null && rs.next())
            {
                LocalTime hiExistente = rs.getTime("ofc_hora_inicio").toLocalTime();
                LocalTime hfExistente = rs.getTime("ofc_hora_termino").toLocalTime(); // Verifica sobreposição de horário
                boolean sobrepoeHoras = !(horaFim.isBefore(hiExistente) || horaInicio.isAfter(hfExistente));
                if (sobrepoeHoras) {
                    return true; // conflito encontrado
                }
            }
        }
        catch (SQLException e) {
            System.err.println("Erro ao verificar conflito de horário: " + e.getMessage());
        }
        return false; // sem conflito
    }


    public List<Map<String, Object>> listarProfessores(Conexao conexao) {
        List<Map<String, Object>> lista = new ArrayList<>();

        String sql = """
        SELECT pe.pes_id AS id, pe.pes_nome AS nome
        FROM professor p
        JOIN pessoa pe ON pe.pes_id = p.pes_id
        ORDER BY pe.pes_nome;
    """;

        ResultSet rs = conexao.consultar(sql);

        try {
            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", rs.getInt("id"));
                item.put("nome", rs.getString("nome"));
                lista.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }



}



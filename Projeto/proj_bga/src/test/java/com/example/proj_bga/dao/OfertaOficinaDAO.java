package com.example.proj_bga.dao;

import com.example.proj_bga.model.OfertaOficina;
import com.example.proj_bga.util.Conexao;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.*;

@Repository
public class OfertaOficinaDAO implements IDAO<OfertaOficina> {

    @Override
    public OfertaOficina gravar(OfertaOficina ofertaOficina, Conexao conexao) {
        String sql = String.format("""
                INSERT INTO ofertaOficina (
                    ofc_hora_inicio, ofc_hora_termino, ofc_dt_inicial, ofc_dt_final, prof_id, ofc_ativo, ofc_fk
                ) VALUES ('%s', '%s', '%s', '%s', %d, '%s', %d)
                RETURNING ofc_id
                """,
                ofertaOficina.getHoraInicio().toString(),
                ofertaOficina.getHoraTermino().toString(),
                new java.sql.Date(ofertaOficina.getDataInicio().getTime()),
                new java.sql.Date(ofertaOficina.getDataFim().getTime()),
                ofertaOficina.getProfessor(),
                ofertaOficina.getAtivo(),
                ofertaOficina.getOfc_fk()
        );

        ResultSet resultado = conexao.consultar(sql);

        try {
            if (resultado != null && resultado.next()) {
                ofertaOficina.setId(resultado.getInt("ofc_id"));
                return ofertaOficina;
            } else {
                System.out.println("Erro: ResultSet nulo ou nenhum registro retornado.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao gravar Oficina: " + e.getMessage());
        }

        return null;
    }

    @Override
    public OfertaOficina alterar(OfertaOficina ofertaOficina, Conexao conexao) {
        String sql = """
                UPDATE ofertaOficina
                SET ofc_hora_inicio = '#2',
                    ofc_hora_termino = '#3',
                    ofc_dt_inicial = '#4',
                    ofc_dt_final = '#5',
                    prof_id = #6,
                    ofc_ativo = '#7',
                    ofc_pk = #9
                WHERE ofc_id = #8;
                """;

        sql = sql.replace("#2", ofertaOficina.getHoraInicio().toString());
        sql = sql.replace("#3", ofertaOficina.getHoraTermino().toString());
        sql = sql.replace("#4", new java.sql.Date(ofertaOficina.getDataInicio().getTime()).toString());
        sql = sql.replace("#5", new java.sql.Date(ofertaOficina.getDataFim().getTime()).toString());
        sql = sql.replace("#6", String.valueOf(ofertaOficina.getProfessor()));
        sql = sql.replace("#7", String.valueOf(ofertaOficina.getAtivo()));
        sql = sql.replace("#8", String.valueOf(ofertaOficina.getId()));
        sql = sql.replace("#9", String.valueOf(ofertaOficina.getOfc_fk()));

        if (conexao.manipular(sql)) {
            return ofertaOficina;
        } else {
            System.out.println("Erro ao alterar Oficina: " + conexao.getMensagemErro());
            return null;
        }
    }

    @Override
    public boolean excluir(OfertaOficina ofertaOficina, Conexao conexao) {
        if (ofertaOficina == null) return false;
        return inativarOficina(ofertaOficina.getId(), conexao);
    }

    public boolean inativarOficina(int id, Conexao conexao) {
        String sql = """
                UPDATE ofertaOficina
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
    public OfertaOficina get(int id, Conexao conexao) {
        if (id <= 0) return null;

        String sql = "SELECT * FROM ofertaOficina WHERE ofc_id = " + id;
        ResultSet resultado = conexao.consultar(sql);

        try {
            if (resultado.next()) {
                OfertaOficina ofertaOficina = new OfertaOficina(
                        resultado.getInt("ofc_id")
                );

                ofertaOficina.setHoraInicio(resultado.getTime("ofc_hora_inicio").toLocalTime());
                ofertaOficina.setHoraTermino(resultado.getTime("ofc_hora_termino").toLocalTime());
                ofertaOficina.setDataInicio(resultado.getDate("ofc_dt_inicial"));
                ofertaOficina.setDataFim(resultado.getDate("ofc_dt_final"));
                ofertaOficina.setProfessor(resultado.getInt("prof_id"));
                ofertaOficina.setAtivo(resultado.getString("ofc_ativo").charAt(0));
                ofertaOficina.setProfessor(resultado.getInt("ofc_pk"));

                return ofertaOficina;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar Oficina: " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<OfertaOficina> get(String filtro, Conexao conexao) {
        List<OfertaOficina> ofertaOficinas = new ArrayList<>();
        String sql = "SELECT * FROM ofertaOficina";

        if (filtro != null && !filtro.isEmpty()) {
            sql += " WHERE " + filtro;
        }

        ResultSet resultado = conexao.consultar(sql);

        try {
            while (resultado.next()) {
                OfertaOficina ofertaOficina = new OfertaOficina(
                        resultado.getInt("ofc_id")
                );

                ofertaOficina.setHoraInicio(resultado.getTime("ofc_hora_inicio").toLocalTime());
                ofertaOficina.setHoraTermino(resultado.getTime("ofc_hora_termino").toLocalTime());
                ofertaOficina.setDataInicio(resultado.getDate("ofc_dt_inicial"));
                ofertaOficina.setDataFim(resultado.getDate("ofc_dt_final"));
                ofertaOficina.setProfessor(resultado.getInt("prof_id"));
                ofertaOficina.setAtivo(resultado.getString("ofc_ativo").charAt(0));
                ofertaOficina.setProfessor(resultado.getInt("ofc_pk"));

                ofertaOficinas.add(ofertaOficina);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar Oficinas: " + e.getMessage());
        }

        return ofertaOficinas;
    }

    // Retorna uma Oficina pelo ID, sem precisar usar o get(int)
    public OfertaOficina getID(int id, Conexao conexao) {
        return get(id, conexao);
    }

    public boolean existeConflitoDeHorario(int professorId, Date dataInicio, Date dataFim, LocalTime horaInicio, LocalTime horaFim, Conexao conexao) {
        String sql = """ 
                SELECT ofc_hora_inicio, 
                ofc_hora_termino, ofc_dt_inicial,
                 ofc_dt_final FROM ofertaOficina WHERE prof_id = %d 
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



package com.example.proj_bga.dao;

import com.example.proj_bga.model.Aluno;
import com.example.proj_bga.util.Conexao;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AlunoDAO implements IDAO<Aluno>
{
    @Override
    public Object gravar(Aluno aluno, Conexao conexao) {
        String sql = """
                INSERT INTO aluno(
                	alu_dt_entrada, alu_foto, alu_mae, alu_pai, alu_responsavel_pais, alu_conhecimento, alu_pais_convivem, alu_pensao, pes_id)
                	VALUES ('#1', '#2', '#3', '#4', '#5', '#6', '#7', '#8', #9);
                """;
        sql = sql.replace("#1",""+ aluno.getDt_entrada());
        sql = sql.replace("#2", aluno.getFoto());
        sql = sql.replace("#3", aluno.getMae());
        sql = sql.replace("#4", aluno.getPai());
        sql = sql.replace("#5",""+aluno.getResponsavel_pais());
        sql = sql.replace("#6",""+ aluno.getConhecimento());
        sql = sql.replace("#7",""+ aluno.getPais_convivem());
        sql = sql.replace("#8",""+ aluno.getPensao());
        sql = sql.replace("#9",""+ aluno.getPes_id());

        try {
            if(conexao.manipular(sql))
                return aluno;
        } catch (Exception e) {
            System.out.println("Erro ao gravar Aluno: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Aluno alterar(Aluno aluno, Conexao conexao) {
        Aluno retorno = null;
        String sql = """
             UPDATE aluno
             SET alu_dt_entrada='#1', alu_foto='#2', alu_mae='#3',
             alu_pai='#4', alu_responsavel_pais='#5', alu_conhecimento='#6',
             alu_pais_convivem='#7', alu_pensao='#8'
             WHERE alu_id=#9;
             """;

        sql = sql.replace("#1",""+aluno.getDt_entrada());
        sql = sql.replace("#2", aluno.getFoto());
        sql = sql.replace("#3", aluno.getMae());
        sql = sql.replace("#4", aluno.getPai());
        sql = sql.replace("#5",""+ aluno.getResponsavel_pais());
        sql = sql.replace("#6",""+ aluno.getPais_convivem());
        sql = sql.replace("#7",""+ aluno.getConhecimento());
        sql = sql.replace("#8",""+ aluno.getPensao());
        sql = sql.replace("#9",""+ aluno.getId());


        try {
            if(conexao.manipular(sql))
                retorno = aluno;
        } catch (Exception e) {
            System.out.println("Erro ao alterar Aluno: " + e.getMessage());
        }
        return retorno;
    }

    @Override
    public boolean excluir(Aluno aluno, Conexao conexao) {
        String sql = """
                DELETE FROM aluno
                	WHERE alu_id = #1;""";
        sql = sql.replace("#1",""+ aluno.getId());
        try {
            if(conexao.manipular(sql))
                return true;
        } catch (Exception e) {
            System.out.println("Erro ao alterar Aluno: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Aluno> get(String filtro, Conexao conexao) {
        List<Aluno> lista = new ArrayList<>();
        String sql = "Select * from aluno ";
        if(!filtro.isEmpty())
            sql = sql + filtro;

        try {
            ResultSet rs = conexao.consultar(sql);
            while (rs.next()) {
                Aluno aluno = new Aluno();
                aluno.setId(rs.getInt("alu_id"));
                aluno.setDt_entrada(rs.getDate("alu_dt_entrada").toLocalDate());
                aluno.setFoto(rs.getString("alu_foto"));
                aluno.setMae(rs.getString("alu_mae"));
                aluno.setPai(rs.getString("alu_pai"));
                aluno.setResponsavel_pais(rs.getString("alu_responsavel_pais").charAt(0));
                aluno.setConhecimento(rs.getString("alu_conhecimento").charAt(0));
                aluno.setPais_convivem(rs.getString("alu_pais_convivem").charAt(0));
                aluno.setPensao(rs.getString("alu_pensao").charAt(0));
                aluno.setPes_id(rs.getInt("pes_id"));
                lista.add(aluno);
            }
            return lista;
        } catch (SQLException e) {
            System.out.println("Erro ao buscar aluno: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Aluno get(int id, Conexao conexao){
        Aluno a = null;
        String sql = """
                SELECT * FROM aluno
                WHERE alu_id = #1;
            """;

        sql = sql.replace("#1", "" + id);

        try {
            ResultSet rs = conexao.consultar(sql);
            if(rs.next()){
                a = new Aluno();
                a.setId(rs.getInt("alu_id"));
                a.setDt_entrada(rs.getDate("alu_dt_entrada").toLocalDate());
                a.setMae(rs.getString("alu_mae"));
                a.setPai(rs.getString("alu_pai"));
                a.setResponsavel_pais(rs.getString("alu_responsavel_pais").charAt(0));
                a.setConhecimento(rs.getString("alu_conhecimento").charAt(0));
                a.setPais_convivem(rs.getString("alu_pais_convivem").charAt(0));
                a.setPensao(rs.getString("alu_pensao").charAt(0));
                a.setPes_id(rs.getInt("pes_id"));
            }
        } catch(Exception e) {
            System.out.println("Erro ao obter Aluno: " + e.getMessage());
        }
        return a;
    }

    public boolean verificarAtivoExistente(int id, Conexao conexao)
    {
        String sql = """
                SELECT P.PES_ID
                FROM PESSOA P
                INNER JOIN ALUNO A ON A.PES_ID = P.PES_ID
                WHERE P.PES_ATIVO = 'S'
                AND A.ALU_ID = #
                """;
        sql = sql.replace("#",""+id);
        ResultSet rs = conexao.consultar(sql);
        try {
            if(rs.next())
                return true;
        } catch (Exception e) {
            System.out.println("Erro ao encontrar Aluno: " + e.getMessage());
        }
        return false;
    }
}

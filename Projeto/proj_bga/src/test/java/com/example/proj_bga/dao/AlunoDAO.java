package com.example.proj_bga.dao;
import com.example.proj_bga.model.Aluno;
import com.example.proj_bga.model.Passeio;
import com.example.proj_bga.util.SingletonDB;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AlunoDAO implements IDAO<Aluno>
{

    @Override
    public Object gravar(Aluno aluno) {
        String sql = """
                INSERT INTO aluno(
                	alu_dt_entrada, alu_foto, alu_mae, alu_pai, alu_responsavel_pais, alu_conhecimento, alu_pais_convivem, alu_pensao, pes_id)
                	VALUES (#1, #2, #3, #4, #5, #6, #7, #8, #9);
                """;
        sql = sql.replace("#1",""+ aluno.getDt_entrada());
        sql = sql.replace("#2",""+ aluno.getFoto());
        sql = sql.replace("#3",""+ aluno.getMae());
        sql = sql.replace("#4",""+ aluno.getPai());
        sql = sql.replace("#5",""+ aluno.getResponsavel_pais());
        sql = sql.replace("#6",""+ aluno.getPais_convivem());
        sql = sql.replace("#7",""+ aluno.getConhecimento());
        sql = sql.replace("#8",""+ aluno.getPensao());
        sql = sql.replace("#9",""+ aluno.getPes_id());


        try {
            if(SingletonDB.getConexao().manipular(sql))
                return aluno;
        } catch (Exception e) {
            System.out.println("Erro ao gravar Aluno: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Object alterar(Aluno aluno) {
        String sql = """
             UPDATE aluno
             SET alu_dt_entrada=#1, alu_foto=#2, alu_mae=#3, 
             alu_pai=#3, alu_responsavel_pais=#4, alu_conhecimento=5, 
             alu_pais_convivem=#6, alu_pensao=#7
             WHERE alu_id=#9;
             """;

        sql = sql.replace("#1",""+ aluno.getDt_entrada());
        sql = sql.replace("#2",""+ aluno.getFoto());
        sql = sql.replace("#3",""+ aluno.getMae());
        sql = sql.replace("#4",""+ aluno.getPai());
        sql = sql.replace("#5",""+ aluno.getResponsavel_pais());
        sql = sql.replace("#6",""+ aluno.getPais_convivem());
        sql = sql.replace("#7",""+ aluno.getConhecimento());
        sql = sql.replace("#8",""+ aluno.getPensao());
        sql = sql.replace("#9",""+ aluno.getId());


        try {
            if(SingletonDB.getConexao().manipular(sql))
                return aluno;
        } catch (Exception e) {
            System.out.println("Erro ao alterar Aluno: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean excluir(Aluno aluno) {
        String sql = """
                DELETE FROM aluno
                	WHERE alu_id = #1;""";
        sql = sql.replace("#1",""+ aluno.getId());
        try {
            if(SingletonDB.getConexao().manipular(sql))
                return true;
        } catch (Exception e) {
            System.out.println("Erro ao alterar Aluno: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Aluno> get(String filtro) {
        List<Aluno> lista = new ArrayList<>();
        String sql = "Select * from aluno";
        if(!filtro.isEmpty())
            sql = sql + " where " + filtro;
        ResultSet rs = SingletonDB.getConexao().consultar(sql);

        try {
            if (rs.next()) {
                Aluno aluno = new Aluno();
                aluno.setId(rs.getInt("alu_cod"));
                aluno.setDt_entrada(rs.getDate("alu_dt_entrada").toLocalDate());
                aluno.setMae(rs.getString("alu_mae"));
                aluno.setPai(rs.getString("alu_pai"));
                aluno.setResponsavel_pais(rs.getString("alu_responsavel_pais"));
                aluno.setConhecimento(rs.getString("alu_conhecimento"));
                aluno.setPais_convivem(rs.getString("alu_pais_convivem"));
                aluno.setPensao(rs.getString("alu_pensao"));
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
    public Aluno get(int id){
        Aluno a = null;
        String sql = """
                SELECT * FROM aluno
                WHERE alu_id = #1;
            """;

        sql = sql.replace("#1", "" + id);

        try {
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            if(rs.next()){
                a = new Aluno();
                a.setId(rs.getInt("alu_id"));
                a.setDt_entrada(rs.getDate("alu_dt_entrada").toLocalDate());
                a.setMae(rs.getString("alu_mae"));
                a.setPai(rs.getString("alu_pai"));
                a.setResponsavel_pais(rs.getString("alu_responsavel_pais"));
                a.setConhecimento(rs.getString("alu_conhecimento"));
                a.setPais_convivem(rs.getString("alu_pais_convivem"));
                a.setPensao(rs.getString("alu_pensao"));
                a.setPes_id(rs.getInt("pes_id"));
            }
        } catch(Exception e) {
            System.out.println("Erro ao obter Aluno: " + e.getMessage());
        }
        return a;
    }

    public boolean verificarAtivoExistente(int id)
    {
        String sql = """
                SELECT P.PES_ID
                FROM PESSOA P
                INNER JOIN ALUNO A ON A.PES_ID = P.PES_ID
                WHERE P.PES_ATIVO = 'A'
                AND A.ALU_ID = #
                """;
        sql = sql.replace("#",""+id);
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            if(rs.next())
                return true;
        } catch (Exception e) {
            System.out.println("Erro ao alterar Aluno: " + e.getMessage());
        }
        return false;
    }
}

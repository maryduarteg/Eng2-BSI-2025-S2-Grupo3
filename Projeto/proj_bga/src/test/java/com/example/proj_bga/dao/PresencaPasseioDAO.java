package com.example.proj_bga.dao;

import com.example.proj_bga.model.PresencaPasseio;
import com.example.proj_bga.util.Conexao;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class PresencaPasseioDAO implements IDAO<PresencaPasseio> {

    @Override
    public Object gravar(PresencaPasseio entidade, Conexao conexao) {
        return gravarFalta(entidade, conexao);
    }

    public PresencaPasseio gravarFalta(PresencaPasseio p, Conexao c) {
        try {
            // 🔹 Verifica se a falta já existe
            String checkSql = """
                SELECT COUNT(*) AS total
                FROM presenca_passeio
                WHERE aluno_alu_id = %d AND dias_marcados_passeio_dmp_id = %d
            """.formatted(p.getIdAluno(), p.getIdDia());

            ResultSet rs = c.consultar(checkSql);
            if (rs.next() && rs.getInt("total") > 0) {
                return p; // já existe, não insere
            }

            // 🔹 Insere nova falta
            String insertSql = """
                INSERT INTO presenca_passeio (aluno_alu_id, dias_marcados_passeio_dmp_id)
                VALUES (%d, %d)
            """.formatted(p.getIdAluno(), p.getIdDia());

            if (c.manipular(insertSql)) return p;
        } catch (Exception e) {
            System.out.println("Erro ao gravar falta: " + e.getMessage());
        }
        return null;
    }

    public List<PresencaPasseio> getPorDia(int idDia, Conexao conexao) {
        List<PresencaPasseio> lista = new ArrayList<>();
        String sql = """
            SELECT * FROM presenca_passeio
            WHERE dias_marcados_passeio_dmp_id = %d
        """.formatted(idDia);

        try {
            ResultSet rs = conexao.consultar(sql);
            while (rs.next()) {
                PresencaPasseio p = new PresencaPasseio();
                p.setId(rs.getInt("psp_id"));
                p.setIdAluno(rs.getInt("aluno_alu_id"));
                p.setIdDia(rs.getInt("dias_marcados_passeio_dmp_id"));
                lista.add(p);
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar PresencaPasseio: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Object alterar(PresencaPasseio entidade, Conexao conexao) { return null; }

    @Override
    public boolean excluir(PresencaPasseio entidade, Conexao conexao) { return false; }

    @Override
    public List<PresencaPasseio> get(String filtro, Conexao conexao) { return null; }

    @Override
    public PresencaPasseio get(int id, Conexao conexao) { return null; }

    public List<Map<String, Object>> listarDatas(Conexao c) {
        List<Map<String, Object>> lista = new ArrayList<>();
        ResultSet rs = c.consultar("""
            SELECT d.dmp_id,
                   pd.pde_descricao AS nome_passeio,
                   d.dmp_data
            FROM dias_marcados_passeio d
            JOIN passeio p ON p.pas_id = d.pas_id
            JOIN passeio_descricao pd ON pd.pde_id = p.pde_id
            ORDER BY d.dmp_data DESC
        """);

        try {
            while (rs.next()) {
                lista.add(Map.of(
                        "dmp_id", rs.getInt("dmp_id"),
                        "descricao", rs.getString("nome_passeio") + " - " + rs.getDate("dmp_data")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Map<String, Object>> listarAlunosPorDia(int dmp_id, Conexao c) {
        List<Map<String, Object>> lista = new ArrayList<>();
        ResultSet rs = c.consultar("""
            SELECT DISTINCT a.alu_id,
                            p.pes_nome,
                            CASE WHEN pp.psp_id IS NULL THEN false ELSE true END AS faltou
            FROM r_aluno_passeio r
            JOIN aluno a ON a.alu_id = r.alu_id
            JOIN pessoa p ON p.pes_id = a.pes_id
            JOIN dias_marcados_passeio d ON d.pas_id = r.pas_id
            LEFT JOIN presenca_passeio pp
                   ON pp.aluno_alu_id = a.alu_id
                  AND pp.dias_marcados_passeio_dmp_id = d.dmp_id
            WHERE d.dmp_id = %d
            ORDER BY p.pes_nome
        """.formatted(dmp_id));

        try {
            while (rs.next()) {
                lista.add(Map.of(
                        "alu_id", rs.getInt("alu_id"),
                        "nome", rs.getString("pes_nome"),
                        "faltou", rs.getBoolean("faltou")
                ));
            }
        } catch (Exception ignore) {}

        return lista;
    }

    public boolean chamadaFeita(int dmp_id, Conexao c) {
        String sql = """
            SELECT COUNT(*) AS total
            FROM presenca_passeio
            WHERE dias_marcados_passeio_dmp_id = %d
        """.formatted(dmp_id);

        try {
            ResultSet rs = c.consultar(sql);
            if (rs.next()) return rs.getInt("total") > 0;
        } catch (Exception e) {
            System.out.println("Erro ao verificar chamada: " + e.getMessage());
        }

        return false;
    }

    public boolean excluirFalta(int alu_id, int dmp_id, Conexao c) {
        String sql = """
            DELETE FROM presenca_passeio
            WHERE aluno_alu_id = %d AND dias_marcados_passeio_dmp_id = %d
        """.formatted(alu_id, dmp_id);

        try {
            return c.manipular(sql);
        } catch (Exception e) {
            System.out.println("Erro ao excluir falta: " + e.getMessage());
            return false;
        }
    }
}

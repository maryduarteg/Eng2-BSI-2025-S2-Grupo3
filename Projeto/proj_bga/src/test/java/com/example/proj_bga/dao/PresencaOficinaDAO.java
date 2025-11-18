package com.example.proj_bga.dao;

import com.example.proj_bga.model.PresencaOficina;
import com.example.proj_bga.util.Conexao;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public Integer buscarOficinaPorDia(int idDia, Conexao conexao) {
        String sql = String.format("""
        SELECT ofc_id 
        FROM dias_marcados_oficinas
        WHERE dmf_id = %d
    """, idDia);

        try {
            ResultSet rs = conexao.consultar(sql);
            if (rs.next()) {
                return rs.getInt("ofc_id");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar oficina pelo dia: " + e.getMessage());
        }

        return null; // se não achou ou deu erro
    }

    public PresencaOficina gravarFalta(PresencaOficina p, Conexao c) {
        String sql = """
            INSERT INTO presenca_oficina (alu_id, ofc_id, dmf_id)
            VALUES (%d, %d, %d)
        """.formatted(p.getIdAluno(), p.getIdOficina(), p.getIdDia());

        if (c.manipular(sql)) return p;
        return null;
    }

    public List<PresencaOficina> listarFaltasPorDia(int idOficina, int idDia, Conexao c) {
        List<PresencaOficina> lista = new ArrayList<>();

        String sql = """
            SELECT pso_id, alu_id, ofc_id, dmf_id
            FROM presenca_oficina
            WHERE ofc_id = %d AND dmf_id = %d
        """.formatted(idOficina, idDia);

        try {
            ResultSet rs = c.consultar(sql);
            while (rs.next()) {
                lista.add(new PresencaOficina(
                        rs.getInt("pso_id"),
                        rs.getInt("alu_id"),
                        rs.getInt("ofc_id"),
                        rs.getInt("dmf_id")
                ));
            }
        } catch (Exception ignore) {}

        return lista;
    }


    public List<Map<String, Object>> listarDatas(Conexao c) {
        List<Map<String, Object>> lista = new ArrayList<>();

        ResultSet rs = c.consultar("""
        SELECT d.dmf_id,
               ofc.ofc_descricao AS nome_oficina,
               d.dmf_dia
        FROM dias_marcados_oficinas d
        JOIN ofertar_oficina o ON o.ofc_id = d.ofc_id
        JOIN oficina ofc ON ofc.ofc_id = o.ofc_pk
        ORDER BY d.dmf_dia DESC
    """);

        try {
            while (rs.next()) {
                lista.add(Map.of(
                        "dmf_id", rs.getInt("dmf_id"),
                        "descricao", rs.getString("nome_oficina") + " - " + rs.getDate("dmf_dia")
                ));
            }
        } catch (Exception ignore) {}

        return lista;
    }


    public List<Map<String, Object>> listarAlunosPorDia(int dmf_id, Conexao c) {
        List<Map<String, Object>> lista = new ArrayList<>();

        ResultSet rs = c.consultar("""
        SELECT a.alu_id,
               p.pes_nome,
               CASE WHEN po.pso_id IS NULL THEN false ELSE true END AS faltou
        FROM r_aluno_oficina r
        JOIN aluno a ON a.alu_id = r.alu_id
        JOIN pessoa p ON p.pes_id = a.pes_id
        JOIN dias_marcados_oficinas d ON d.ofc_id = r.ofc_id
        LEFT JOIN presenca_oficina po 
               ON po.alu_id = a.alu_id 
              AND po.dmf_id = d.dmf_id
        WHERE d.dmf_id = %d
        ORDER BY p.pes_nome
    """.formatted(dmf_id));

        try {
            while (rs.next()) {
                lista.add(Map.of(
                        "alu_id", rs.getInt("alu_id"),
                        "nome", rs.getString("pes_nome"),
                        "faltou", rs.getBoolean("faltou") // <- AQUI!
                ));
            }
        } catch (Exception ignore) {}

        return lista;
    }



    public boolean chamadaFeita(int dmf_id, Conexao c) {
        // Busca o id da oficina associada ao dia
        Integer idOficina = buscarOficinaPorDia(dmf_id, c);
        if (idOficina == null) return false;

        String sql = """
        SELECT COUNT(*) AS total
        FROM presenca_oficina
        WHERE ofc_id = %d AND dmf_id = %d
    """.formatted(idOficina, dmf_id);

        try {
            ResultSet rs = c.consultar(sql);
            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
        } catch (Exception e) {
            System.out.println("Erro ao verificar chamada: " + e.getMessage());
        }
        return false;
    }


    public boolean excluirFalta(int alu_id, int dmf_id, Conexao c) {
        Integer idOficina = buscarOficinaPorDia(dmf_id, c);
        if (idOficina == null) return false;

        String sql = """
        DELETE FROM presenca_oficina
        WHERE alu_id = %d AND ofc_id = %d AND dmf_id = %d
    """.formatted(alu_id, idOficina, dmf_id);

        try {
            return c.manipular(sql);
        } catch (Exception e) {
            System.out.println("Erro ao excluir falta: " + e.getMessage());
            return false;
        }
    }


}

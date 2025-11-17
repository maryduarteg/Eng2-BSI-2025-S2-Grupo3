package com.example.proj_bga.dao;

import com.example.proj_bga.model.FotosOficina;
import com.example.proj_bga.util.Conexao;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Base64;

@Repository
public class FotosOficinaDAO implements IDAO<FotosOficina> {

    @Override
    public FotosOficina gravar(FotosOficina f, Conexao conexao) {
        String sql = """
                    INSERT INTO fotos_oficina (ofc_id, dmf_id, fto_numero, fto_foto, fto_nome_arquivo, fto_descricao)
                    VALUES (#1, #2, #3, decode('#4', 'base64'), '#5', '#6');
                """;
        sql = sql.replace("#1", "" + f.getOfc_id());
        sql = sql.replace("#2", "" + f.getDmf_id());
        sql = sql.replace("#3", "" + f.getFto_numero());
        String fotoBase64 = Base64.getEncoder().encodeToString(f.getFto_foto());
        sql = sql.replace("#4", fotoBase64);
        String nomeArquivo = f.getFto_nome_arquivo().replace("'", "''");
        String descricao = f.getFto_descricao().replace("'", "''");
        sql = sql.replace("#5", nomeArquivo);
        sql = sql.replace("#6", descricao);

        if (conexao.manipular(sql)) {
            return f;
        }
        return null;
    }

    @Override
    public FotosOficina alterar(FotosOficina f, Conexao conexao) {
        String sql = """
                    UPDATE fotos_oficina
                    SET fto_foto = decode('#1', 'base64'),
                        fto_nome_arquivo = '#2',
                        fto_descricao = '#3',
                        fto_data_upload = CURRENT_TIMESTAMP
                    WHERE ofc_id = #4 AND dmf_id = #5 AND fto_numero = #6;
                """;
        String fotoBase64 = Base64.getEncoder().encodeToString(f.getFto_foto());
        sql = sql.replace("#1", fotoBase64);
        String nomeArquivo = f.getFto_nome_arquivo().replace("'", "''");
        String descricao = f.getFto_descricao().replace("'", "''");
        sql = sql.replace("#2", nomeArquivo);
        sql = sql.replace("#3", descricao);
        sql = sql.replace("#4", "" + f.getOfc_id());
        sql = sql.replace("#5", "" + f.getDmf_id());
        sql = sql.replace("#6", "" + f.getFto_numero());

        if (conexao.manipular(sql)) {
            return f;
        }
        return null;
    }

    @Override
    public boolean excluir(FotosOficina f, Conexao conexao) {
        String sql = """
                    DELETE FROM fotos_oficina
                    WHERE ofc_id = #1 AND dmf_id = #2 AND fto_numero = #3;
                """;
        sql = sql.replace("#1", "" + f.getOfc_id());
        sql = sql.replace("#2", "" + f.getDmf_id());
        sql = sql.replace("#3", "" + f.getFto_numero());
        return conexao.manipular(sql);
    }

    @Override
    public List<FotosOficina> get(String filtro, Conexao conexao) {
        List<FotosOficina> fotos = new ArrayList<>();
        String sql = "SELECT * FROM fotos_oficina";
        if (filtro != null && !filtro.isEmpty()) {
            sql += " WHERE " + filtro;
        }

        ResultSet rs = conexao.consultar(sql);
        try {
            while (rs != null && rs.next()) {
                FotosOficina foto = new FotosOficina();
                foto.setOfc_id(rs.getInt("ofc_id"));
                foto.setDmf_id(rs.getInt("dmf_id"));
                foto.setFto_numero(rs.getInt("fto_numero"));
                foto.setFto_nome_arquivo(rs.getString("fto_nome_arquivo"));
                foto.setFto_data_upload(rs.getDate("fto_data_upload"));
                foto.setFto_descricao(rs.getString("fto_descricao"));
                foto.setFto_foto(rs.getBytes("fto_foto"));
                fotos.add(foto);
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        return fotos;
    }

    @Override
    public FotosOficina get(int id, Conexao conexao) {
        String sql = "SELECT * FROM fotos_oficina WHERE fto_numero = " + id + " LIMIT 1";
        ResultSet rs = conexao.consultar(sql);
        try {
            if (rs != null && rs.next()) {
                FotosOficina foto = new FotosOficina();
                foto.setOfc_id(rs.getInt("ofc_id"));
                foto.setDmf_id(rs.getInt("dmf_id"));
                foto.setFto_numero(rs.getInt("fto_numero"));
                foto.setFto_nome_arquivo(rs.getString("fto_nome_arquivo"));
                foto.setFto_data_upload(rs.getDate("fto_data_upload"));
                foto.setFto_descricao(rs.getString("fto_descricao"));
                foto.setFto_foto(rs.getBytes("fto_foto"));
                return foto;
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());;
        }
        return null;
    }

    public FotosOficina get(int alu_id, int ofc_id, int pso_id, int dmf_id, int fto_numero, Conexao conexao) {
        String sql = """
                    SELECT * FROM fotos_oficina
                    WHERE ofc_id = #1 AND dmf_id = #2 AND fto_numero = #3;
                """;
        sql = sql.replace("#1", "" + ofc_id);
        sql = sql.replace("#2", "" + dmf_id);
        sql = sql.replace("#3", "" + fto_numero);

        ResultSet rs = conexao.consultar(sql);
        try {
            if (rs != null && rs.next()) {
                FotosOficina foto = new FotosOficina();
                foto.setOfc_id(rs.getInt("ofc_id"));
                foto.setDmf_id(rs.getInt("dmf_id"));
                foto.setFto_numero(rs.getInt("fto_numero"));
                foto.setFto_nome_arquivo(rs.getString("fto_nome_arquivo"));
                foto.setFto_data_upload(rs.getDate("fto_data_upload"));
                foto.setFto_descricao(rs.getString("fto_descricao"));
                foto.setFto_foto(rs.getBytes("fto_foto"));
                return foto;
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        return null;
    }
}

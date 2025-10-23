package com.example.proj_bga.dao;

import com.example.proj_bga.model.Oficina;
import com.example.proj_bga.model.Usuario;
import com.example.proj_bga.util.SingletonDB;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UsuarioDAO implements IDAO<Usuario>{
    @Override
    public Usuario gravar(Usuario usuario) {
        String sql = String.format("""
        INSERT INTO usuario (usr_login, usr_senha, ctsr_id, usr_ativo) 
        VALUES ('%s', '%s', '%d', '%d')
        RETURNING usr_id
        """,
                usuario.getLogin(),
                usuario.getSenha(),
                usuario.getCategariaUsuarioId(),
                usuario.getIsAtivo() ? 1 : 0
        );

        ResultSet resultado = SingletonDB.getConexao().consultar(sql);

        try {
            if (resultado != null && resultado.next()) {
                usuario.setId(resultado.getInt("usr_id"));
                return usuario;
            } else {
                System.out.println("Erro: ResultSet nulo ou nenhum registro retornado.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao gravar Usuário: " + e.getMessage());
        }

        return null;
    }

    @Override
    public Object alterar(Usuario entidade) {
        return null;
    }

    @Override
    public boolean excluir(Usuario entidade) {
        return false;
    }

    @Override
    public List<Usuario> get(String filtro) {
        return List.of();
    }

    @Override
    public Usuario get(int id) {
        return null;
    }

    public Usuario getUsuario(String login) {
        String sql = "SELECT * FROM usuario WHERE usr_login = '" + login + "'";
        System.out.println(sql);
        ResultSet resultado = SingletonDB.getConexao().consultar(sql);


        try {
            if (resultado == null)
                return null;

            if (resultado.next()) {
                Usuario usuario = new Usuario(
                        resultado.getInt("usr_id"),
                        resultado.getString("usr_login"),
                        resultado.getString("usr_senha"),
                        resultado.getInt("ctsr_id"),
                        resultado.getBoolean("usr_ativo")
                );

                return usuario;
            }

            return null;
        } catch (SQLException e) {
            System.out.println("Erro ao buscar Oficina por ID: " + e.getMessage());
        }
        return null;
    }
}

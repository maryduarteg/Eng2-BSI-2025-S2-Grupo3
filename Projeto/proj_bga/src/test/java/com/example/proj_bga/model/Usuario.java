package com.example.proj_bga.model;

import com.example.proj_bga.dao.UsuarioDAO;
import com.example.proj_bga.util.Conexao;
import com.example.proj_bga.util.SingletonDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class Usuario {
    @Autowired
    private UsuarioDAO dao;

    private int id;
    private String login;
    private String senha;
    private int categariaUsuarioId;
    private boolean isAtivo;

    public Usuario(){}

    public Usuario(int id, String login, String senha, int categariaUsuarioId, boolean isAtivo) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.categariaUsuarioId = categariaUsuarioId;
        this.isAtivo = isAtivo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getCategariaUsuarioId() {
        return categariaUsuarioId;
    }

    public void setCategariaUsuarioId(int categariaUsuarioId) {
        this.categariaUsuarioId = categariaUsuarioId;
    }

    public boolean getIsAtivo() {
        return isAtivo;
    }

    public void setLogin(boolean isAtivo) {
        this.isAtivo = isAtivo;
    }

    public boolean logar(String login, String senha){
        Conexao conexao = SingletonDB.conectar();
        Usuario usuario = dao.getUsuario(login,  conexao);
        if(usuario == null)
            return false;

        if (usuario.getSenha().equals(senha) && usuario.getIsAtivo())
            return true;

        return false;
    }
}

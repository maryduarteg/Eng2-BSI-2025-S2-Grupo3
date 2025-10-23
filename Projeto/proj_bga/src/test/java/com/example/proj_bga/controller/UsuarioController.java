package com.example.proj_bga.controller;

import com.example.proj_bga.model.Oficina;
import com.example.proj_bga.model.Usuario;
import com.example.proj_bga.util.Conexao;
import com.example.proj_bga.util.SingletonDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

@Service
public class UsuarioController {

    @Autowired
    private Usuario usuarioModel;

    public boolean logar(String login, String senha) {
        Conexao conexao = new Conexao();
        return usuarioModel.logar(login, senha);
    }
}
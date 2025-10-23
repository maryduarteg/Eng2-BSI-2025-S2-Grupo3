package com.example.proj_bga.controller;

import com.example.proj_bga.model.Oficina;
import com.example.proj_bga.model.Usuario;
import com.example.proj_bga.util.Conexao;
import com.example.proj_bga.util.SingletonDB;
import com.example.proj_bga.util.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

@Service
public class UsuarioController {

    @Autowired
    private Usuario usuarioModel;

    public Map<String, Object> logar(String login, String senha) {
        Map<String, Object> response = new HashMap<>();

        if(usuarioModel.logar(login, senha))
        {
            response.put("isLogado", true);
            response.put("token", Token.gerarToken(login));
            response.put("categariaUsuarioId", usuarioModel.getCategariaUsuarioId());
            response.put("isAtivo", usuarioModel.getIsAtivo());
        }
        else{
            response.put("isLogado", false);
            response.put("token", "");
            response.put("categariaUsuarioId", "");
            response.put("isAtivo", usuarioModel.getIsAtivo());
        }

        return response;
    }
}
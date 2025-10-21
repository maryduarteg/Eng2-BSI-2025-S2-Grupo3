package com.example.proj_bga.controller;

import com.example.proj_bga.model.Aluno;
import com.example.proj_bga.util.Conexao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.example.proj_bga.model.Aluno.*;
@Service
public class AlunoController {
    @Autowired
    private Aluno funcionarioModel;
   /* public Map<String, Object> addAluno(String nome, String cpf, String senha, String email, String login, int nivel) {
        Conexao conexao = new Conexao();
        Aluno novo = new Aluno(nome, cpf, senha, email, login, nivel);
        Aluno gravado = Aluno.gravar(novo);

        if (gravado != null) {
            Map<String, Object> json = new HashMap<>();
            json.put("id", gravado.getId());
            json.put("nome", gravado.getNome());
            json.put("cpf", gravado.getCpf());
            json.put("email", gravado.getEmail());
            json.put("login", gravado.getLogin());
            json.put("nivel", gravado.getNivel());
            json.put("senha", gravado.getSenha());
            return json;
        } else {
            return Map.of("erro", "Erro ao cadastrar o produto");
        }

    }*/
}

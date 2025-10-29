package com.example.proj_bga.controller;

import com.example.proj_bga.model.Aluno;
import com.example.proj_bga.model.Pessoa;
import com.example.proj_bga.util.Conexao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PessoaController {
    @Autowired
    private Pessoa pessoaModel;
    public List<Map<String, Object>> getAll() {
        Conexao conexao = new Conexao();
        List<Pessoa> pessoas = pessoaModel.getAll();
        if(pessoas == null)
            return null;

        List<Map<String, Object>> resultado = new ArrayList<>();
        for(Pessoa e: pessoas){
            Map<String, Object> json = new HashMap<>();
            json.put("id", e.getId());
            json.put("nome", e.getNome());
            resultado.add(json);
        }
        return resultado;
    }
}

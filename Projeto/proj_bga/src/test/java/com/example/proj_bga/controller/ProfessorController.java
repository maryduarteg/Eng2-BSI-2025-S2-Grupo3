package com.example.proj_bga.controller;

import com.example.proj_bga.model.Aluno;
import com.example.proj_bga.model.Pessoa;
import com.example.proj_bga.model.Professor;
import com.example.proj_bga.util.Conexao;
import com.example.proj_bga.util.SingletonDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProfessorController {
    @Autowired
    Pessoa pessoaModel = new Pessoa();
    @Autowired
    private Professor professorModel;

    public List<Map<String, Object>> getProfessores(String filtro) {
        Conexao conexao = SingletonDB.conectar();
        Pessoa pessoa = null;
        List<Professor> profs = professorModel.consultar(filtro, conexao);
        if(profs == null){
            return null;
        }

        List<Map<String, Object>> resultado = new ArrayList<>();
        for(Professor e: profs){
            Map<String, Object> json = new HashMap<>();
            json.put("id", e.getId());
            json.put("matricula", e.getMatricula());
            json.put("pes_id", e.getPes_id());
            pessoa = pessoaModel.getId(e.getPes_id(), conexao);
            json.put("nome", pessoa.getNome());
            resultado.add(json);
        }
        return resultado;
    }
}

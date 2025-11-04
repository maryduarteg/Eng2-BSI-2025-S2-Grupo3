package com.example.proj_bga.controller;

import com.example.proj_bga.model.Pessoa;
import com.example.proj_bga.util.Conexao;
import com.example.proj_bga.util.SingletonDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PessoaController {
    @Autowired
    private Pessoa pessoaModel;
    public List<Map<String, Object>> getAll() {
        Conexao conexao = SingletonDB.conectar();
        List<Pessoa> pessoas = pessoaModel.getAll(conexao);
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

    public Map<String, Object> getByID(int id) {
        Conexao conexao = SingletonDB.conectar();

        Pessoa p = pessoaModel.getId(id, conexao); // pega a pessoa do DAO

        if (p == null) {
            return null; // ou pode lançar exceção / retornar Map com "erro"
        }

        Map<String, Object> json = new HashMap<>();
        json.put("id", p.getId());
        json.put("nome", p.getNome());
        json.put("cpf", p.getcpf());
        json.put("dtnasc", p.getDt_nascimento());
        json.put("rg", p.getRg());
        json.put("ativo", p.getAtivo());
        json.put("end", p.getEnd_id());

        return json;
    }

    public Map<String, Object> atualizarPessoa(int id,
                                              String nome, String cpf, LocalDate dtnasc,
                                              String rg, char ativo, int end)
    {
        Conexao conexao = SingletonDB.conectar();

        if(id <= 0 || dtnasc == null || nome.isEmpty()|| cpf.isEmpty() ||
                rg.isEmpty() || end <= 0 || ativo == '\u0000')
            return Map.of("erro", "Dados inválidos!!");

        Pessoa encontrado = pessoaModel.getId(id, conexao);
        if(encontrado == null) {
            return Map.of("erro", "Aluno não encontrado");
        }

        encontrado.setNome(nome);
        encontrado.setDt_nascimento(dtnasc);
        encontrado.setcpf(cpf);
        encontrado.setRg(rg);
        encontrado.setAtivo(ativo);
        encontrado.setEnd_id(end);

        boolean resp = pessoaModel.alterarPessoa(encontrado, conexao);

        return Map.of("resposta", resp);

    }
}

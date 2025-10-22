package com.example.proj_bga.controller;

import com.example.proj_bga.model.Aluno;
import com.example.proj_bga.model.Passeio;
import com.example.proj_bga.util.Conexao;
import com.example.proj_bga.util.SingletonDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static com.example.proj_bga.model.Aluno.*;
@Service
public class AlunoController {
    @Autowired
    private Aluno alunoModel;
   public Map<String, Object> addAluno(
           int id, LocalDate dt_entrada, String foto,
           String mae, String pai, String responsavel_pais,
           String conhecimento, String pais_convivem,
           String pensao, int pes_id)
   {
        Aluno novo = new Aluno(0, dt_entrada, foto, mae, pai, responsavel_pais, conhecimento,
                pais_convivem, pensao, pes_id);
        Aluno gravado = alunoModel.gravar(novo);

        if (gravado != null) {
            Map<String, Object> json = new HashMap<>();
            json.put("Id", gravado.getId());
            json.put("dt_entrada", gravado.getDt_entrada());
            json.put("foto", gravado.getFoto());
            json.put("mae", gravado.getMae());
            json.put("pai", gravado.getPai());
            json.put("responsavel/pais", gravado.getResponsavel_pais());
            json.put("conhecimento", gravado.getConhecimento());
            json.put("pais_convivem", gravado.getPais_convivem());
            json.put("pensao", gravado.getPensao());
            json.put("pes_id", gravado.getPes_id());
            return json;
        } else {
            return Map.of("erro", "Erro ao cadastrar o aluno");
        }
    }

    public Map<String, Object> deletarAluno(int id) {
        boolean resp = alunoModel.consultarAtivo(id);

        if (!resp)
        {
            return Map.of("erro", "Aluno não encontrado para exclusão.");
        }
        else
        {
            Aluno aux = alunoModel.consultar("where alu_id = "+id);
            boolean deletado = alunoModel.deletarAluno(aux);
            if (deletado)
            {
                return Map.of("mensagem", "Aluno " + id + " removido com sucesso!");
            } else {
                String erroReal = SingletonDB.getConexao().getMensagemErro();
                System.err.println("Falha no DELETE. Erro: " + erroReal);

                return Map.of("erro", erroReal);
            }
        }
    }
}

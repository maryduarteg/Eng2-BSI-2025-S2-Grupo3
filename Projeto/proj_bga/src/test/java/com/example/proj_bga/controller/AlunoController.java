package com.example.proj_bga.controller;

import com.example.proj_bga.model.Aluno;
import com.example.proj_bga.model.Pessoa;
import com.example.proj_bga.util.Conexao;
import com.example.proj_bga.util.SingletonDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class AlunoController {
    @Autowired
    private Aluno alunoModel;
   public Map<String, Object> addAluno(LocalDate dt_entrada, String foto,
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
    public List<Map<String, Object>> getAlunos(String filtro) {
        Conexao conexao = new Conexao();
        List<Aluno> alunos = alunoModel.consultar(filtro, conexao);
        if(alunos == null){
            return null;
        }

        List<Map<String, Object>> resultado = new ArrayList<>();
        for(Aluno e: alunos){
            Map<String, Object> json = new HashMap<>();
            json.put("Id", e.getId());
            json.put("dt_entrada", e.getDt_entrada());
            json.put("foto", e.getFoto());
            json.put("mae", e.getMae());
            json.put("pai", e.getPai());
            json.put("responsavel/pais", e.getResponsavel_pais());
            json.put("conhecimento", e.getConhecimento());
            json.put("pais_convivem", e.getPais_convivem());
            json.put("pensao", e.getPensao());
            json.put("pes_id", e.getPes_id());
            resultado.add(json);
        }
        return resultado;
    }

    public Map<String, Object> atualizarAluno(int id,
            LocalDate dt_entrada, String foto,
            String mae, String pai, String responsavel_pais,
            String conhecimento, String pais_convivem,
            String pensao, int pes_id)
    {
        if(id <= 0 || dt_entrada == null || foto.isEmpty()|| mae.isEmpty() ||
                pai.isEmpty() || pes_id <= 0 || responsavel_pais.isEmpty()
                || conhecimento.isEmpty() || pais_convivem.isEmpty() || pensao.isEmpty()) {
            return Map.of("erro", "Dados inválidos!!");
        }
        Aluno encontrado = alunoModel.consultar("WHERE ALU_ID = "+id);
        if(encontrado == null) {
            return Map.of("erro", "Aluno não encontrado");
        }

        encontrado.setDt_entrada(dt_entrada);
        encontrado.setFoto(foto);
        encontrado.setMae(mae);
        encontrado.setPai(pai);
        encontrado.setResponsavel_pais(responsavel_pais);
        encontrado.setConhecimento(conhecimento);
        encontrado.setPais_convivem(pais_convivem);
        encontrado.setPensao(pensao);
        encontrado.setPes_id(pes_id);

        Aluno atualizado = alunoModel.update(encontrado);
        if(atualizado == null) {
            return Map.of(
                    "id", atualizado.getId(),
                    "dt_nasc", atualizado.getDt_entrada(),
                    "data", atualizado.getFoto(),
                    "hora_inicio", atualizado.getMae(),
                    "hora_final", atualizado.getPai(),
                    "chamada_feita",  atualizado.getResponsavel_pais(),
                    "pde_id", atualizado.getConhecimento(),
                    "pde_id", atualizado.getPais_convivem(),
                    "pde_id", atualizado.getPensao(),
                    "pde_id", atualizado.getPes_id()
            );
        } else {
            return Map.of("erro", "Erro ao atualizar aluno");
        }
    }



    public Map<String, Object> deletarAluno(int id) {
        boolean resp = alunoModel.consultarAtivo(id);

        if (!resp)
            return Map.of("erro", "Aluno não encontrado para exclusão.");
        else
        {
            Pessoa pessoaModel = new Pessoa();
            Aluno aux = alunoModel.consultar("WHERE ALU_ID = "+id);
            Pessoa paux = pessoaModel.getId(aux.getPes_id());
            paux.setAtivo('N');
            boolean deletado = pessoaModel.alterarPessoa(paux);

            if (deletado)
                return Map.of("mensagem", "Aluno " + id + " removido com sucesso!");
            else
            {
                String erroReal = SingletonDB.getConexao().getMensagemErro();
                System.err.println("Falha no DELETE. Erro: " + erroReal);
                return Map.of("erro", erroReal);
            }
        }
    }
}

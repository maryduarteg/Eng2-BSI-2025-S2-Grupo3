package com.example.proj_bga.controller;

import com.example.proj_bga.model.Aluno;
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
public class AlunoController {
    @Autowired
    Pessoa pessoaModel = new Pessoa();
    @Autowired
    private Aluno alunoModel;

   public Map<String, Object> addAluno(LocalDate dt_entrada, String foto,
           String mae, String pai, char responsavel_pais,
           char conhecimento, char pais_convivem,
           char pensao, int pes_id)
   {
       Conexao conexao = SingletonDB.conectar();

       Aluno novo = new Aluno(0, dt_entrada, foto, mae, pai, responsavel_pais, conhecimento,
                pais_convivem, pensao, pes_id);
        Aluno gravado = alunoModel.gravar(novo, conexao);

        if (gravado != null) {
            Map<String, Object> json = new HashMap<>();
            json.put("id", gravado.getId());
            json.put("dt_entrada", gravado.getDt_entrada());
            json.put("foto", gravado.getFoto());
            json.put("mae", gravado.getMae());
            json.put("pai", gravado.getPai());
            json.put("responsavel_pais", gravado.getResponsavel_pais());
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
        Conexao conexao = SingletonDB.conectar();
        Pessoa pessoa = null;
        List<Aluno> alunos = alunoModel.consultar(filtro, conexao);
        if(alunos == null){
            return null;
        }

        List<Map<String, Object>> resultado = new ArrayList<>();
        for(Aluno e: alunos){
            Map<String, Object> json = new HashMap<>();
            json.put("id", e.getId());
            json.put("dt_entrada", e.getDt_entrada());
            json.put("foto", e.getFoto());
            json.put("mae", e.getMae());
            json.put("pai", e.getPai());
            json.put("responsavel_pais", e.getResponsavel_pais());
            json.put("conhecimento", e.getConhecimento());
            json.put("pais_convivem", e.getPais_convivem());
            json.put("pensao", e.getPensao());
            json.put("pes_id", e.getPes_id());
            pessoa = pessoaModel.getId(e.getPes_id(), conexao);
            if(pessoa == null)
                json.put("ativo", "-");
            else
                json.put("ativo", pessoa.getAtivo());

            resultado.add(json);
        }
        return resultado;
    }

    public Map<String, Object> atualizarAluno(int id,
            LocalDate dt_entrada, String foto,
            String mae, String pai, char responsavel_pais,
            char conhecimento, char pais_convivem,
            char pensao, int pes_id)
    {
        Pessoa pessoa = null;
        if(id <= 0 || dt_entrada == null || foto.isEmpty()|| mae.isEmpty() ||
                pai.isEmpty() || pes_id <= 0 || responsavel_pais == '\u0000'
                || (conhecimento == '\u0000') || pais_convivem == '\u0000' || pensao== '\u0000') {
            return Map.of("erro", "Dados inválidos!!");
        }
        Conexao conexao = SingletonDB.conectar();
        Aluno encontrado = alunoModel.consultar(id, conexao);
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

        Aluno atualizado = alunoModel.update(encontrado, conexao);
        if(atualizado != null) {
            return Map.of(
                    "id", atualizado.getId(),
                    "dt_nasc", atualizado.getDt_entrada(),
                    "foto", atualizado.getFoto(),
                    "hora_inicio", atualizado.getMae(),
                    "hora_final", atualizado.getPai(),
                    "responsavel_pais",  atualizado.getResponsavel_pais(),
                    "conhecimento", atualizado.getConhecimento(),
                    "pais_convivem", atualizado.getPais_convivem(),
                    "pensao", atualizado.getPensao(),
                    "pes_id", atualizado.getPes_id()

            );
        } else {
            return Map.of("erro", "Erro ao atualizar aluno");
        }
    }

    public Map<String, Object> deletarAluno(int id) {
        Conexao conexao = SingletonDB.conectar();
        boolean resp = alunoModel.consultarAtivo(id, conexao);

        if (!resp)
            return Map.of("erro", "Aluno não encontrado para exclusão.");
        else
        {
            Aluno aux = alunoModel.consultar(id, conexao);
            Pessoa paux = pessoaModel.getId(aux.getPes_id(), conexao);
            System.out.println(aux.getId() +" "+aux.getPes_id());
            paux.setAtivo('N');
            boolean deletado = pessoaModel.alterarPessoa(paux, conexao);

            if (deletado)
                return Map.of("mensagem", "Aluno " + id + " removido com sucesso!");
            else
            {
                String erroReal = conexao.getMensagemErro();
                System.err.println("Falha no DELETE. Erro: " + erroReal);
                return Map.of("erro", erroReal);
            }
        }
    }
}

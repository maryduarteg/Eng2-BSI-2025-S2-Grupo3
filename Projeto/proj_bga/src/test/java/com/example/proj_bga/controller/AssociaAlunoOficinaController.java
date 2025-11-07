package com.example.proj_bga.controller;

import com.example.proj_bga.model.Aluno;
import com.example.proj_bga.model.AssociaAlunoOficina;
import com.example.proj_bga.util.Conexao;
import com.example.proj_bga.util.SingletonDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class AssociaAlunoOficinaController {

    @Autowired
    private Aluno alunoModel;

    @Autowired
    private AssociaAlunoOficina associaAlunoOficinaModel;
    @Autowired
    private NativeWebRequest nativeWebRequest;

    //vincular aluno à ofcina
    public Map<String, Object> addAlunoOficina(int alu_id, int ofc_id) {
        Conexao conexao = new Conexao();

        if(alu_id == 0 || ofc_id == 0)
            return Map.of("erro", "Dados inválidos para cadastro!!");

        AssociaAlunoOficina associaAlunoOficina = new AssociaAlunoOficina(alu_id, ofc_id);
        AssociaAlunoOficina associacaoGravada = associaAlunoOficinaModel.gravaAlunoOficina(associaAlunoOficina, conexao);

        if(associacaoGravada != null){
            Map<String, Object> json = new HashMap<>();
            json.put("aluId",  associacaoGravada.getAlu_id());
            json.put("ofcId", associacaoGravada.getOfc_id());
            return json;
        }
        return Map.of("erro", "Houve um erro!!");
    }

    //remover vinculo do aluno com a oficina
    public Map<String, Object> deletarAlunoOficina(int alu_id, int  ofc_id) {
        Conexao conexao = new Conexao();
        boolean alunoExisteEAtivo = alunoModel.consultarAtivo(alu_id, conexao);

        if(!alunoExisteEAtivo){
            String erroAluno = conexao.getMensagemErro();
            if (erroAluno != null && !erroAluno.isEmpty()) {
                return Map.of("erro", "Erro ao verificar aluno: " + erroAluno);
            }
            return Map.of("erro", "Aluno não encontrado ou inativo!");
        }

        AssociaAlunoOficina assAluOfc = new  AssociaAlunoOficina(alu_id, ofc_id);
        boolean excluida = associaAlunoOficinaModel.excluiAlunoOficina(assAluOfc, conexao);

        if(excluida){
            return Map.of("mensagem", "Vinculo excluido com sucesso");
        }
        else{
            String erro = conexao.getMensagemErro();
            if (erro == null || erro.isEmpty()) {
                erro = "Não foi possível excluir o vínculo.";
            }
            System.out.println("Erro:" + erro);
            return Map.of("erro", erro);
        }
    }

    //listar alunos e seus vinculos
    public List<Map<String, Object>> getAlunoOficina() {
        Conexao conexao = new Conexao();
        List<AssociaAlunoOficina> associaAlunoOficinaList = associaAlunoOficinaModel.consultarAlunosOficinas("", conexao);
        if(associaAlunoOficinaList == null){
            System.out.println("Erro!");
            return null;
        }

        List<Map<String, Object>> resultado = new ArrayList<>();
        for (AssociaAlunoOficina a :  associaAlunoOficinaList) {
            Map<String, Object> json = new HashMap<>();
            json.put("aluId", a.getAlu_id());
            json.put("ofcId", a.getOfc_id());
            resultado.add(json);
        }

        return resultado;
    }
}
package com.example.proj_bga.controller;

import com.example.proj_bga.model.AssociaAlunoOficina;
import com.example.proj_bga.model.DiasOficina;
import com.example.proj_bga.util.Conexao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class AssociaAlunoOficinaController {

    @Autowired
    private AssociaAlunoOficina associaAlunoOficinaModel;

    //associar alunos à oficinas
    public Map<String, DiasOficina> addAlunoOficina(int alu_id, int ofc_id) {
        Conexao conexao = new Conexao();

        if(alu_id == 0 || ofc_id == 0)
            return Map.of("erro", "Dados inválidos para cadastro!!");

        AssociaAlunoOficina associaAlunoOficina = new AssociaAlunoOficina(alu_id, ofc_id);

        AssociaAlunoOficina associacaoGravada = associaAlunoOficina.gravaAlunoOficina(associaAlunoOficina, conexao);
        if(associacaoGravada != null){
            Map<String, Object> json = new HashMap<>();
            json.put("aluId",  associaAlunoOficina.getAlu_id());
            json.put("ofcId", associaAlunoOficina.getOfc_id());
            return json;
        }

    }
}

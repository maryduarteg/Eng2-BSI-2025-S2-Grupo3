package com.example.proj_bga.controller;

import com.example.proj_bga.model.DiasMarcados;
import com.example.proj_bga.model.Passeio;
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

public class DiasMarcadosController {
    @Autowired
    DiasMarcados diasModel = new DiasMarcados();


    public List<Map<String, Object>> getDias(String filtro) {
        Conexao conexao = SingletonDB.conectar();
        List<DiasMarcados> lista = diasModel.consultar(filtro, conexao);
        if (lista == null) {
            return null;
        }

        List<Map<String, Object>> resultado = new ArrayList<>();
        for (DiasMarcados e : lista) {
            Map<String, Object> json = new HashMap<>();
            json.put("id", e.getId());
            json.put("data", e.getData());
            json.put("idofc", e.getIdOFc());

            resultado.add(json);
        }

        return resultado;
    }

    public Map<String, Object> deletarDia(int id) {
        Conexao conexao = SingletonDB.conectar();

        DiasMarcados dia = diasModel.consultarID(id,conexao);
        if (dia == null) {
            return Map.of("erro", "Data não encontrada para exclusão.");
        }
        boolean deletado = diasModel.deletar(dia, conexao);
        if (deletado) {
            return Map.of("mensagem", "Data " + id + " removida com sucesso!");
        } else {
            String erroReal = SingletonDB.conectar().getMensagemErro();
            System.err.println("Falha no DELETE. Erro: " + erroReal);
            return Map.of("erro", erroReal);
        }
    }
}

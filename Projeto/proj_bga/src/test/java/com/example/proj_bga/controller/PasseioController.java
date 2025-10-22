package com.example.proj_bga.controller;

import com.example.proj_bga.model.Passeio;
import com.example.proj_bga.util.Conexao;
import com.example.proj_bga.util.SingletonDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

import java.util.*;

@Service
public class PasseioController {
    @Autowired
    private Passeio passeioModel;

    public Map<String, Object> addPass(Date pas_data, LocalTime pas_hora_inicio, LocalTime pas_hora_final, String pas_chamada_feita, int pde_id) {
        if(pas_data == null || pas_hora_inicio == null || pas_hora_final == null || pas_chamada_feita.isEmpty() || pde_id <= 0) {
            return Map.of("erro", "Dados inválidos para cadastro!!");
        }

        Passeio novo = new Passeio(pas_data, pas_hora_inicio, pas_hora_final, pas_chamada_feita, pde_id);

        Passeio gravado = passeioModel.gravarPasseio(novo);

        if(gravado != null) {
            Map<String, Object> json = new HashMap<>();
            json.put("id", gravado.getPasId());
            json.put("pas_data", gravado.getPas_data());
            json.put("hora_inicio", gravado.getPas_hora_inicio());
            json.put("hora_final", gravado.getPas_hora_final());
            json.put("chamada_feita",  gravado.getPas_chamada_feita());
            json.put("pde_id", gravado.getPde_id());
            return json;
        }
        String erroReal = "Erro desconhecido ao cadastrar passeio.";
        try {
            erroReal = SingletonDB.getConexao().getMensagemErro();
        } catch (Exception e) {
            System.err.println("Falha ao obter mensagem de erro do DB: " + e.getMessage());
        }
        return Map.of("erro", erroReal);
    }

    public List<Map<String, Object>> getPass() {
        Conexao conexao = new Conexao();
        List<Passeio> passeios = passeioModel.consultarPasseio("", conexao);
        if(passeios == null){
            return null;
        }

        List<Map<String, Object>> resultado = new ArrayList<>();
        for(Passeio e: passeios){
            Map<String, Object> json = new HashMap<>();
            json.put("id", e.getPasId());
            json.put("data", e.getPas_data());
            json.put("hora_inicio", e.getPas_hora_inicio());
            json.put("hora_final", e.getPas_hora_final());
            json.put("chamada_feita", e.getPas_chamada_feita());
            json.put("pde_id", e.getPde_id());
            resultado.add(json);
        }
        return resultado;
    }

    public Map<String, Object> deletarPasseio(int id) {
        Passeio passeio = passeioModel.consultarPasseioID(id);

        if (passeio == null) {
            return Map.of("erro", "Passeio não encontrado para exclusão.");
        }

        boolean deletado = passeioModel.deletarPasseio(passeio);

        if (deletado) {
            return Map.of("mensagem", "Passeio " + id + " removido com sucesso!");
        } else {
            String erroReal = SingletonDB.getConexao().getMensagemErro();
            System.err.println("Falha no DELETE. Erro: " + erroReal);

            return Map.of("erro", erroReal);
        }
    }

    public Map<String, Object> updatePasseio(int pas_id, Date pas_data, LocalTime pas_hora_inicio, LocalTime pas_hora_final, String pas_chamada_feita, int pde_id, int pde) {
        if(pas_id <= 0 || pas_data == null || pas_hora_inicio == null || pas_hora_final == null || pas_chamada_feita.isEmpty() || pde_id <= 0) {
            return Map.of("erro", "Dados inválidos!!");
        }

        Passeio encontrado = passeioModel.consultarPasseioID(pas_id);
        if(encontrado == null) {
            return Map.of("erro", "Passeio não encontrado");
        }

        encontrado.setPas_data(pas_data);
        encontrado.setPas_hora_inicio(pas_hora_inicio);
        encontrado.setPas_hora_final(pas_hora_final);
        encontrado.setPas_chamada_feita(pas_chamada_feita);
        encontrado.setPde_id(pde_id);

        Passeio atualizado = passeioModel.alterarPasseio(encontrado);
        if(atualizado != null) {
            return Map.of(
                    "id", atualizado.getPas_data(),
                    "data", atualizado.getPas_data(),
                    "hora_inicio", atualizado.getPas_hora_inicio(),
                    "hora_final", atualizado.getPas_hora_final(),
                    "chamada_feita",  atualizado.getPas_chamada_feita(),
                    "pde_id", atualizado.getPde_id()
            );
        } else {
            String erroReal = SingletonDB.getConexao().getMensagemErro();
            System.err.println("Falha no UPDATE. Erro: " + erroReal);
            return Map.of("erro", erroReal);
        }
    }
}

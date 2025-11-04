package com.example.proj_bga.controller;

import com.example.proj_bga.model.Passeio;
import com.example.proj_bga.util.Conexao;
import com.example.proj_bga.util.SingletonDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PasseioController {
    @Autowired
    private Passeio passeioModel;

    public Map<String, Object> addPass(String pde_descricao) {
        Conexao conexao = SingletonDB.conectar();

        if(pde_descricao == null) {
            return Map.of("erro", "Dados inválidos para cadastro!!");
        }

        Passeio novo = new Passeio(0, pde_descricao);
        Passeio gravado = passeioModel.gravarPasseio(novo, conexao);

        if(gravado != null) {
            Map<String, Object> json = new HashMap<>();
            json.put("pde_id", gravado.getPdeId());
            json.put("pde_descricao", gravado.getDescricao());
            return json;
        }

        String erroReal = "Erro desconhecido ao cadastrar passeio.";
        try {
            erroReal = SingletonDB.conectar().getMensagemErro();
        } catch (Exception e) {
            System.err.println("Falha ao obter mensagem de erro do DB: " + e.getMessage());
        }
        return Map.of("erro", erroReal);
    }

    public List<Map<String, Object>> getPassFiltro(String filtro) {
        Conexao conexao = SingletonDB.conectar();
        List<Passeio> passeios = passeioModel.consultarPasseio(filtro, conexao);
        if(passeios == null){
            return null;
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for(Passeio e: passeios){
            Map<String, Object> json = new HashMap<>();
            json.put("pde_id", e.getPdeId());
            json.put("pde_descricao", e.getDescricao());
            result.add(json);
        }
        return result;
    }

    public Map<String, Object> deletarPasseio(int id) {
        Conexao conexao = SingletonDB.conectar();

        Passeio passeio = passeioModel.consultarPasseioID(id,conexao);
        if (passeio == null) {
            return Map.of("erro", "Passeio não encontrado para exclusão.");
        }
        boolean deletado = passeioModel.deletarPasseio(passeio, conexao);
        if (deletado) {
            return Map.of("mensagem", "Passeio " + id + " removido com sucesso!");
        } else {
            String erroReal = SingletonDB.conectar().getMensagemErro();
            System.err.println("Falha no DELETE. Erro: " + erroReal);
            return Map.of("erro", erroReal);
        }
    }

    public Map<String, Object> updatePasseio(int pde_id, String pde_descricao) {
        Conexao conexao = SingletonDB.conectar();

        if(pde_id <= 0 || pde_descricao.isEmpty()) {
            return Map.of("erro", "Dados inválidos!!");
        }

        Passeio encontrado = passeioModel.consultarPasseioID(pde_id, conexao);
        if(encontrado == null) {
            return Map.of("erro", "Passeio não encontrado!");
        }
        encontrado.setDescricao(pde_descricao);
        Passeio atualizado = passeioModel.alterarPasseio(encontrado, conexao);

        if(atualizado != null) {
            return Map.of(
                    "id", atualizado.getPdeId(),
                    "descricao", atualizado.getDescricao()
            );
        } else {
            String erroDB = SingletonDB.conectar().getMensagemErro();
            System.err.println("Erro de atualização no DB: " + erroDB);
            return Map.of("erro", "Erro ao atualizar o Passeio! Detalhe: " + erroDB);
        }
    }
}

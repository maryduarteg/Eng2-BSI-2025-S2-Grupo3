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
        if(pde_descricao == null) {
            return Map.of("erro", "Dados inválidos para cadastro!!");
        }

        Passeio novo = new Passeio(pde_descricao);
        Passeio gravado = passeioModel.gravarPasseio(novo);
        if(gravado != null) {
            Map<String, Object> json = new HashMap<>();
            json.put("pde_id", gravado.getPdeId());
            json.put("pde_descricao", gravado.getDescricao());
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

    public List<Map<String, Object>> getPassFiltro(String filtro) {
        Conexao conexao = new Conexao();
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

    public Map<String, Object> updatePasseio(int pde_id, String pde_descricao) {
        if(pde_id <= 0 || pde_descricao.isEmpty()) {
            return Map.of("erro", "Dados inválidos!!");
        }
        Passeio encontrado = passeioModel.consultarPasseioID(pde_id);
        if(encontrado == null) {
            return Map.of("erro", "Passeio não encontrado");
        }
        encontrado.setDescricao(pde_descricao);
        Passeio atualizado = passeioModel.alterarPasseio(encontrado);
        if(atualizado != null) {
            return Map.of(
                    "descricao", atualizado.getDescricao()
            );
        } else {
            String erroReal = SingletonDB.getConexao().getMensagemErro();
            System.err.println("Falha no UPDATE. Erro: " + erroReal);
            return Map.of("erro", erroReal);
        }
    }
}

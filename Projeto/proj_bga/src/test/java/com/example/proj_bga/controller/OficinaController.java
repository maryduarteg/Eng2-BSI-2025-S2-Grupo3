package com.example.proj_bga.controller;
import com.example.proj_bga.model.OfertaOficina;
import com.example.proj_bga.model.Oficina;
import com.example.proj_bga.util.Conexao;
import com.example.proj_bga.util.SingletonDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

@Service
public class OficinaController {
    @Autowired
    private Oficina OficinaModel;

    public Map<String, Object> addOficina(String descricao)
    {
        Conexao conexao = SingletonDB.conectar();
        if(descricao == null ){
            return Map.of("erro", "Dados inválidos para cadastro!!");
        }

        Oficina novo =  new Oficina(0,descricao);

        Oficina gravada = OficinaModel.gravarOficina(novo, conexao);
        if(gravada != null){
            Map<String, Object> json = new HashMap<>();
            json.put("idOficina", novo.getId());
            json.put("descricao", novo.getDescricao());
            return json;
        }
        String erroReal = "Erro desconhecido ao cadastrar oficina.";
        try {
            erroReal = conexao.getMensagemErro();
        } catch (Exception e)
        {
            System.err.println("Falha ao obter mensagem de erro do DB: " + e.getMessage());

        }
        return Map.of("erro", erroReal);
    }

    public Map<String, Object> updateOficina(int id, String descricao) {
        Conexao conexao = SingletonDB.conectar();
        if(descricao == null ) {
            return Map.of("erro", "Dados inválidos!!");
        }

        Oficina encontrada = OficinaModel.consultarOficinasID(id, conexao);
        if(encontrada == null) {
            return Map.of("erro", "Oficina não encontrada");
        }

        encontrada.setId(id);
        encontrada.setDescricao(descricao);


        Oficina atualizada = OficinaModel.alterarOficina(encontrada, conexao);

        if(atualizada != null) { // Atualização ocorreu com sucesso
            return Map.of(
                    "idOficina", atualizada.getId(),
                    "descricao", atualizada.getDescricao()
            );
        } else {
            return Map.of("erro", "Erro ao atualizar Oficina");
        }
    }

    public Map<String, Object> getOficinaPorId(int id, Conexao conexao) {
        Oficina o = OficinaModel.consultarOficinasID(id,conexao); // pega a oficina do DAO

        if (o == null) {
            return null; // ou pode lançar exceção / retornar Map com "erro"
        }

        Map<String, Object> json = new HashMap<>();
        json.put("idOficina", o.getId());
        json.put("descricao", o.getDescricao());

        return json;
    }

    public List<Map<String, Object>> getOficina(){
        Conexao conexao = new Conexao();
        List<Oficina> lista = OficinaModel.consultarOficinas("", conexao);
        if(lista == null){
            return null;
        }

        List<Map<String, Object>> resultado = new ArrayList<>();
        for (Oficina o: lista)
        {
            Map<String, Object> json = new HashMap<>();
            json.put("idOficina", o.getId());
            json.put("descricao", o.getDescricao());

            resultado.add(json);
        }
        return resultado;
    }
}

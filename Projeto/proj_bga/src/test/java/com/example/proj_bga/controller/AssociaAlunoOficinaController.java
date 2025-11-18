package com.example.proj_bga.controller;

import com.example.proj_bga.dao.OficinaDAO;
import com.example.proj_bga.model.*;
import com.example.proj_bga.util.Conexao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
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
    private Oficina oficinaModel;

    @Autowired
    private OfertaOficina ofertaOficinaModel;

    public Map<String, Object> addAlunoOficina(int alu_id, int ofc_id) {
        Conexao conexao = new Conexao();

        if(alu_id == 0 || ofc_id == 0) {
            return Map.of("erro", "Dados inválidos para cadastro!!");
        }

        AssociaAlunoOficina associaAlunoOficina = new AssociaAlunoOficina(alu_id, ofc_id);
        AssociaAlunoOficina associacaoGravada = associaAlunoOficinaModel.gravaAlunoOficina(associaAlunoOficina, conexao);

        if(associacaoGravada != null){
            Map<String, Object> json = new HashMap<>();
            json.put("aluId", associacaoGravada.getAlu_id());
            json.put("ofcId", associacaoGravada.getOfc_id());
            return json;
        }

        return Map.of("erro", "Houve um erro!!");
    }

    public Map<String, Object> deletarAlunoOficina(int alu_id, int ofc_id) {
        Conexao conexao = new Conexao();

        boolean alunoExisteEAtivo = alunoModel.consultarAtivo(alu_id, conexao);

        if(!alunoExisteEAtivo){
            String erroAluno = conexao.getMensagemErro();
            if (erroAluno != null && !erroAluno.isEmpty()) {
                return Map.of("erro", "Erro ao verificar aluno: " + erroAluno);
            }
            return Map.of("erro", "Aluno não encontrado ou inativo!");
        }

        AssociaAlunoOficina assAluOfc = new AssociaAlunoOficina(alu_id, ofc_id);
        boolean excluida = associaAlunoOficinaModel.excluiAlunoOficina(assAluOfc, conexao);

        if(excluida){
            return Map.of("mensagem", "Vinculo excluido com sucesso");
        } else {
            String erro = conexao.getMensagemErro();
            if (erro == null || erro.isEmpty()) {
                erro = "Não foi possível excluir o vínculo.";
            }
            return Map.of("erro", erro);
        }
    }

    public List<Map<String, Object>> getAlunoOficina() {
        Conexao conexao = new Conexao();
        List<AssociaAlunoOficina> associaAlunoOficinaList = associaAlunoOficinaModel.consultarAlunosOficinas("", conexao);

        if(associaAlunoOficinaList == null){
            return new ArrayList<>();
        }

        List<Map<String, Object>> resultado = new ArrayList<>();

        for (AssociaAlunoOficina a : associaAlunoOficinaList) {
            Map<String, Object> json = new HashMap<>();
            json.put("aluId", a.getAlu_id());
            json.put("ofcId", a.getOfc_id());
            resultado.add(json);
        }

        return resultado;
    }

    public List<Map<String, Object>> getOficinas() {
        Conexao conexao = new Conexao();
        List<Oficina> oficinas = oficinaModel.consultarOficinas("", conexao);
        List<Map<String, Object>> lista = new ArrayList<>();

        for (Oficina oficina : oficinas) {
            List<OfertaOficina> ofertas = ofertaOficinaModel.consultarOficinas("", conexao);

            for (OfertaOficina oferta : ofertas) {
                if (oferta.getOfc_fk() == oficina.getId() && oferta.getAtivo() == 'S') {
                    Map<String, Object> json = new HashMap<>();

                    json.put("id", oferta.getId());
                    json.put("idOferta", oferta.getId());
                    json.put("idOficina", oficina.getId());
                    json.put("descricao", oficina.getDescricao());
                    json.put("horaInicio", oferta.getHoraInicio());
                    json.put("horaFim", oferta.getHoraTermino());
                    json.put("dataInicio", oferta.getDataInicio());
                    json.put("dataFim", oferta.getDataFim());
                    json.put("ativo", String.valueOf(oferta.getAtivo()));
                    json.put("professor", oferta.getProfessor());

                    lista.add(json);
                }
            }
        }
        return lista;
    }


    public Map<String, Object> getOficinaComOferta(int oficinaId) {
        Conexao conexao = new Conexao();
        OficinaDAO oficinaDAO = new OficinaDAO();
        Oficina oficina = oficinaDAO.get(oficinaId, conexao);

        if (oficina == null) {
            return Map.of("erro", "Oficina não encontrada");
        }

        List<OfertaOficina> ofertas = ofertaOficinaModel.consultarOficinas("", conexao);
        OfertaOficina ofertaAtiva = null;

        if (ofertas != null) {
            for (OfertaOficina oferta : ofertas) {
                if (oferta.getOfc_fk() == oficinaId && oferta.getAtivo() == 'S') {
                    ofertaAtiva = oferta;
                    break;
                }
            }
        }

        Map<String, Object> json = new HashMap<>();
        json.put("idOficina", oficina.getId());
        json.put("descricao", oficina.getDescricao());
        json.put("ativo", String.valueOf(oficina.getAtivo()));

        if (ofertaAtiva != null) {
            json.put("horaInicio", ofertaAtiva.getHoraInicio() != null ? ofertaAtiva.getHoraInicio().toString() : null);
            json.put("horaFim", ofertaAtiva.getHoraTermino() != null ? ofertaAtiva.getHoraTermino().toString() : null);
            json.put("dataInicio", ofertaAtiva.getDataInicio() != null ? ofertaAtiva.getDataInicio().toString() : null);
            json.put("dataFim", ofertaAtiva.getDataFim() != null ? ofertaAtiva.getDataFim().toString() : null);
            json.put("professor", ofertaAtiva.getProfessor());
        }
        else {
            json.put("semOferta", true);
        }

        return json;
    }
}

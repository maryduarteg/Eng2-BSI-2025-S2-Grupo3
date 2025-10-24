package com.example.proj_bga.controller;

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
    private Oficina oficinaModel;

    //adicionar Oficina
    public Map<String, Object> addOficina(String nome, LocalTime horaInicio, LocalTime horaFim, Date dataInicio, Date dataFim, int professor, char ativo)
    {
        if(nome == null || horaInicio == null || horaFim == null || dataInicio == null || dataFim == null || professor == 0){
            return Map.of("erro", "Dados inválidos para cadastro!!");
        }

        Oficina novo =  new Oficina(nome, horaInicio, horaFim, dataInicio, dataFim, professor, ativo);

        Oficina gravada = oficinaModel.gravarOficina(novo);
        if(gravada != null){
            Map<String, Object> json = new HashMap<>();
            json.put("idOficina", novo.getId());
            json.put("hora_inicio", novo.getHoraInicio());
            json.put("hora_termino", novo.getHoraTermino());
            json.put("data_inicio", novo.getDataInicio());
            json.put("data_fim", novo.getDataFim());
            json.put("pde_id", novo.getProfessor());
            json.put("ativo", novo.getAtivo());
            return json;
        }
        String erroReal = "Erro desconhecido ao cadastrar oficina.";
        try {
            erroReal = SingletonDB.getConexao().getMensagemErro();
        } catch (Exception e)
        {
            System.err.println("Falha ao obter mensagem de erro do DB: " + e.getMessage());

        }
        return Map.of("erro", erroReal);
    }

    //Exibir Oficina
    public List<Map<String, Object>> getOficina(){
        Conexao conexao = new Conexao();
        List<Oficina> oficina = oficinaModel.consultarOficinas("", conexao);
        if(oficina == null){
            return null;
    }

        List<Map<String, Object>> resultado = new ArrayList<>();
        for (Oficina o: oficina)
        {
            Map<String, Object> json = new HashMap<>();
            json.put("nome", o.getNome());
            json.put("idOficina", o.getId());
            json.put("hora_inicio", o.getHoraInicio());
            json.put("hora_termino", o.getHoraTermino());
            json.put("data_inicio", o.getDataInicio());
            json.put("data_fim", o.getDataFim());
            json.put("pde_id", o.getProfessor());
            json.put("ativo", o.getAtivo());
            resultado.add(json);
        }
        return resultado;
    }

    // Exibir uma única Oficina por ID
    public Map<String, Object> getOficinaPorId(int id) {
        Oficina o = oficinaModel.consultarOficinasID(id); // pega a oficina do DAO

        if (o == null) {
            return null; // ou pode lançar exceção / retornar Map com "erro"
        }

        Map<String, Object> json = new HashMap<>();
        json.put("nome", o.getNome());
        json.put("idOficina", o.getId());
        json.put("hora_inicio", o.getHoraInicio());
        json.put("hora_termino", o.getHoraTermino());
        json.put("data_inicio", o.getDataInicio());
        json.put("data_fim", o.getDataFim());
        json.put("pde_id", o.getProfessor());
        json.put("ativo", o.getAtivo());

        return json;
    }


    //deletar oficina
//    public Map<String, Object> deletarOficina(int id) {
//        Oficina oficina = oficinaModel.consultarOficinasID(id);
//
//        if (oficina == null) {
//            return Map.of("erro", "Oficina não encontrada para exclusão.");
//        }
//
//        boolean deletada = oficinaModel.deletarOficina(oficina);
//
//        if (deletada) {
//            return Map.of("mensagem", "Oficina " + id + " Inativa com sucesso!");
//        } else {
//            String erroReal = SingletonDB.getConexao().getMensagemErro();
//            System.err.println("Falha no DELETE. Erro: " + erroReal);
//
//            return Map.of("erro", erroReal);
//        }
//    }

    public Map<String, Object> inativarOficina(int id) {
        Oficina oficina = oficinaModel.consultarOficinasID(id);

        if (oficina == null) {
            return Map.of("erro", "Oficina não encontrada para inativação.");
        }

        boolean inativada = oficinaModel.inativarOficina(id);

        if (inativada) {
            return Map.of("mensagem", "Oficina " + id + " inativada com sucesso!");
        } else {
            String erroReal = SingletonDB.getConexao().getMensagemErro();
            System.err.println("Falha ao inativar. Erro: " + erroReal);

            return Map.of("erro", erroReal);
        }
    }



    // Atualizar Oficina
        public Map<String, Object> updateOficina(int id, String nome, LocalTime horaInicio, LocalTime horaFim, Date dataInicio, Date dataFim, int professor, char ativo) {
            if(id <= 0 || nome == null || horaInicio == null || horaFim == null || dataInicio == null || dataFim == null || professor == 0) {
                return Map.of("erro", "Dados inválidos!!");
            }

            Oficina encontrada = oficinaModel.consultarOficinasID(id);
            if(encontrada == null) {
                return Map.of("erro", "Oficina não encontrada");
            }

            encontrada.setNome(nome);
            encontrada.setHoraInicio(horaInicio);
            encontrada.setHoraTermino(horaFim);
            encontrada.setDataInicio(dataInicio);
            encontrada.setDataFim(dataFim);
            encontrada.setProfessor(professor);
            encontrada.setAtivo(ativo);

            Oficina atualizada = oficinaModel.alterarOficina(encontrada);

            if(atualizada != null) { // Atualização ocorreu com sucesso
                return Map.of(
                        "id", atualizada.getId(),
                        "nome", atualizada.getNome(),
                        "horaInicio", atualizada.getHoraInicio(),
                        "horaTermino", atualizada.getHoraTermino(),
                        "dataInicio", atualizada.getDataInicio(),
                        "dataFim", atualizada.getDataFim(),
                        "professor", atualizada.getProfessor(),
                        "ativo", atualizada.getAtivo()
                );
            } else {
                return Map.of("erro", "Erro ao atualizar Oficina");
            }
        }
    }



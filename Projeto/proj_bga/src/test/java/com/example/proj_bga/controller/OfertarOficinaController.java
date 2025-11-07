package com.example.proj_bga.controller;

import com.example.proj_bga.dao.OficinaDAO;
import com.example.proj_bga.model.OfertaOficina;
import com.example.proj_bga.model.Oficina;
import com.example.proj_bga.util.Conexao;
import com.example.proj_bga.util.SingletonDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

@Service
public class OfertarOficinaController {

    @Autowired
    private OfertaOficina ofertaOficinaModel;

    //adicionar Oficina
    public Map<String, Object> addOficina( LocalTime horaInicio, LocalTime horaFim, Date dataInicio, Date dataFim, int professor, char ativo, int ofc_fk)
    {
        Conexao conexao = SingletonDB.conectar();
        if( horaInicio == null || horaFim == null || dataInicio == null || dataFim == null || professor == 0){
            return Map.of("erro", "Dados inválidos para cadastro!!");
        }

        OfertaOficina novo =  new OfertaOficina(horaInicio, horaFim, dataInicio, dataFim, professor, ativo, ofc_fk);

        OfertaOficina gravada = ofertaOficinaModel.gravarOficina(novo, conexao);
        if(gravada != null){
            Map<String, Object> json = new HashMap<>();
            json.put("idOficina", novo.getId());
            json.put("hora_inicio", novo.getHoraInicio());
            json.put("hora_termino", novo.getHoraTermino());
            json.put("data_inicio", novo.getDataInicio());
            json.put("data_fim", novo.getDataFim());
            json.put("pde_id", novo.getProfessor());
            json.put("ativo", novo.getAtivo());
            json.put("ofc_fk", novo.getOfc_fk());
            return json;
        }
        String erroReal = "Erro desconhecido ao cadastrar oferta de oficina.";
        try {
            erroReal = conexao.getMensagemErro();
        } catch (Exception e)
        {
            System.err.println("Falha ao obter mensagem de erro do DB: " + e.getMessage());

        }
        return Map.of("erro", erroReal);
    }

    //Exibir Oficina
    public List<Map<String, Object>> getOficina(){
        Conexao conexao = new Conexao();
        OficinaDAO oficinaDAO = new OficinaDAO();
        List<OfertaOficina> ofertaOficina = ofertaOficinaModel.consultarOficinas("", conexao);
        if(ofertaOficina == null){
            System.out.println("Deu errrroooo aquiii");
            return null;
    }

        List<Map<String, Object>> resultado = new ArrayList<>();
        for (OfertaOficina o: ofertaOficina)
        {
            Map<String, Object> json = new HashMap<>();
            json.put("idOficina", o.getId());
            json.put("hora_inicio", o.getHoraInicio());
            json.put("hora_termino", o.getHoraTermino());
            json.put("data_inicio", o.getDataInicio());
            json.put("data_fim", o.getDataFim());
            json.put("pde_id", o.getProfessor());
            json.put("ativo", o.getAtivo());
            json.put("ofc_fk", o.getOfc_fk());

            Oficina oficina = oficinaDAO.get(o.getOfc_fk(),conexao);
            if(oficina == null)
                json.put("nome", "----");
            else
                json.put("nome", oficina.getDescricao());
            resultado.add(json);
        }
        return resultado;
    }

    // Exibir uma única Oficina por ID
    public Map<String, Object> getOficinaPorId(int id, Conexao conexao) {
        OfertaOficina o = ofertaOficinaModel.consultarOficinasID(id,conexao); // pega a oficina do DAO
        OficinaDAO oficinaDAO = new OficinaDAO();
        if (o == null) {
            return null; // ou pode lançar exceção / retornar Map com "erro"
        }

        Map<String, Object> json = new HashMap<>();
        json.put("idOficina", o.getId());
        json.put("hora_inicio", o.getHoraInicio());
        json.put("hora_termino", o.getHoraTermino());
        json.put("data_inicio", o.getDataInicio());
        json.put("data_fim", o.getDataFim());
        json.put("pde_id", o.getProfessor());
        json.put("ativo", o.getAtivo());
        json.put("ofc_fk", o.getOfc_fk());
        Oficina oficina = oficinaDAO.get(o.getOfc_fk(),conexao);
        if(oficina == null)
            json.put("nome", "----");
        else
            json.put("nome", oficina.getDescricao());

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
        Conexao conexao = SingletonDB.conectar();
        OfertaOficina ofertaOficina = ofertaOficinaModel.consultarOficinasID(id, conexao);

        if (ofertaOficina == null) {
            return Map.of("erro", "Oficina não encontrada para inativação.");
        }

        boolean inativada = ofertaOficinaModel.inativarOficina(id, conexao);

        if (inativada) {
            return Map.of("mensagem", "Oficina " + id + " inativada com sucesso!");
        } else {
            String erroReal = conexao.getMensagemErro();
            System.err.println("Falha ao inativar. Erro: " + erroReal);

            return Map.of("erro", erroReal);
        }
    }



    // Atualizar Oficina
        public Map<String, Object> updateOficina(int id, LocalTime horaInicio, LocalTime horaFim, Date dataInicio, Date dataFim, int professor, char ativo, int ofc_pk) {
            Conexao conexao = SingletonDB.conectar();
            if(id <= 0 || horaInicio == null || horaFim == null || dataInicio == null || dataFim == null || professor == 0) {
                return Map.of("erro", "Dados inválidos!!");
            }

            OfertaOficina encontrada = ofertaOficinaModel.consultarOficinasID(id, conexao);
            if(encontrada == null) {
                return Map.of("erro", "Oficina não encontrada");
            }


            encontrada.setHoraInicio(horaInicio);
            encontrada.setHoraTermino(horaFim);
            encontrada.setDataInicio(dataInicio);
            encontrada.setDataFim(dataFim);
            encontrada.setProfessor(professor);
            encontrada.setAtivo(ativo);
            encontrada.setOfc_fk(ofc_pk);

            OfertaOficina atualizada = ofertaOficinaModel.alterarOficina(encontrada, conexao);

            if(atualizada != null) { // Atualização ocorreu com sucesso
                return Map.of(
                        "id", atualizada.getId(),
                        "horaInicio", atualizada.getHoraInicio(),
                        "horaTermino", atualizada.getHoraTermino(),
                        "dataInicio", atualizada.getDataInicio(),
                        "dataFim", atualizada.getDataFim(),
                        "professor", atualizada.getProfessor(),
                        "ativo", atualizada.getAtivo(),
                        "ofc_pk", atualizada.getOfc_fk()
                );
            } else {
                return Map.of("erro", "Erro ao atualizar Oferta de Oficina");
            }
        }

        public boolean existeConflitoDeHorario(int professorId, Date dataInicio, Date dataFim, LocalTime horaInicio, LocalTime horaFim) {
            Conexao conexao = SingletonDB.conectar();
            return ofertaOficinaModel.verificarConflitoHorario(professorId, dataInicio, dataFim, horaInicio, horaFim, conexao);
        }

        public List<Map<String, Object>> getProfessores(Conexao conexao) {
            return ofertaOficinaModel.listarProfessores(conexao);
        }

}




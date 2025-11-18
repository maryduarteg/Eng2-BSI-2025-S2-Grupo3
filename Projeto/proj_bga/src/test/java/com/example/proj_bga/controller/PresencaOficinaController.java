package com.example.proj_bga.controller;

import com.example.proj_bga.dao.OfertaOficinaDAO;
import com.example.proj_bga.model.PresencaOficina;
import com.example.proj_bga.util.Conexao;
import com.example.proj_bga.util.SingletonDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


import com.example.proj_bga.dao.OficinaDAO;
import com.example.proj_bga.dao.PresencaOficinaDAO;

import java.util.List;
import java.util.Map;

@Service
public class PresencaOficinaController {

    @Autowired
    private PresencaOficinaDAO presencaDAO;

    public PresencaOficina registrarFalta(int idAluno, int idDia) {
        Conexao c = SingletonDB.conectar();

        int idOficina = c.consultarValor("""
            SELECT ofc_id FROM dias_marcados_oficinas WHERE dmf_id = %d
        """.formatted(idDia));

        PresencaOficina p = new PresencaOficina(0, idAluno, idOficina, idDia);
        return presencaDAO.gravarFalta(p, c);
    }

    public List<PresencaOficina> listarFaltas(int idDia) {
        Conexao c = SingletonDB.conectar();

        int idOficina = c.consultarValor("""
            SELECT ofc_id FROM dias_marcados_oficinas WHERE dmf_id = %d
        """.formatted(idDia));

        return presencaDAO.listarFaltasPorDia(idOficina, idDia, c);
    }

    public List<Map<String, Object>> listarDatas() {
        return presencaDAO.listarDatas(SingletonDB.conectar());
    }

    public List<Map<String, Object>> listarAlunos(int dmf_id) {
        return presencaDAO.listarAlunosPorDia(dmf_id, SingletonDB.conectar());
    }

    public boolean chamadaFeita(int dmf_id) {
        Conexao c = SingletonDB.conectar();
        return presencaDAO.chamadaFeita(dmf_id, c);
    }

    public boolean excluirFalta(int alu_id, int dmf_id) {
        Conexao c = SingletonDB.conectar();
        return presencaDAO.excluirFalta(alu_id, dmf_id, c);
    }

}



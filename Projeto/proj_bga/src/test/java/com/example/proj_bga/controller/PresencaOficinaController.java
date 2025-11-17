package com.example.proj_bga.controller;

import com.example.proj_bga.dao.PresencaOficinaDAO;
import com.example.proj_bga.model.PresencaOficina;
import com.example.proj_bga.util.Conexao;
import com.example.proj_bga.util.SingletonDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public int buscarOficinaIdPorDia(int dmf_id) {
        Conexao c = SingletonDB.conectar();
        try {
            int ofc_id = c.consultarValor(
                    "SELECT ofc_id FROM dias_marcados_oficinas WHERE dmf_id = " + dmf_id
            );
            return ofc_id;
        } catch (Exception e) {
            System.out.println("Erro ao buscar oficina por dia: " + e.getMessage());
            return 0;
        }
    }  // CHAVE FALTANDO - ADICIONADA
}  // CHAVE FALTANDO - ADICIONADA

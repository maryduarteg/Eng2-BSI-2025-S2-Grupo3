package com.example.proj_bga.controller;

import com.example.proj_bga.dao.PresencaPasseioDAO;
import com.example.proj_bga.model.PresencaPasseio;
import com.example.proj_bga.util.Conexao;
import com.example.proj_bga.util.SingletonDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class PresencaPasseioController {

    @Autowired
    private PresencaPasseioDAO presencaDAO;

    public PresencaPasseio registrarFalta(int idAluno, int idDia) {
        Conexao c = SingletonDB.conectar();
        PresencaPasseio p = new PresencaPasseio(0, idAluno, idDia);
        return presencaDAO.gravarFalta(p, c);
    }

    public List<Map<String, Object>> listarDatas() {
        return presencaDAO.listarDatas(SingletonDB.conectar());
    }

    public List<Map<String, Object>> listarAlunos(int dmp_id) {
        return presencaDAO.listarAlunosPorDia(dmp_id, SingletonDB.conectar());
    }

    public boolean chamadaFeita(int dmp_id) {
        Conexao c = SingletonDB.conectar();
        return presencaDAO.chamadaFeita(dmp_id, c);
    }

    public boolean excluirFalta(int alu_id, int dmp_id) {
        Conexao c = SingletonDB.conectar();
        return presencaDAO.excluirFalta(alu_id, dmp_id, c);
    }
}

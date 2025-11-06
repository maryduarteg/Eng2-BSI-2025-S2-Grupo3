package com.example.proj_bga.controller;

import com.example.proj_bga.model.PresencaOficina;
import com.example.proj_bga.util.Conexao;
import com.example.proj_bga.util.SingletonDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PresencaOficinaController {

    @Autowired
    private PresencaOficina presencaModel;

    public PresencaOficina registrarFalta(int idAluno, int idOficina, int idDia) {
        Conexao conexao = SingletonDB.conectar();
        PresencaOficina p = new PresencaOficina(0, idAluno, idOficina, idDia);
        return presencaModel.gravarPresenca(p, conexao);
    }

    public List<PresencaOficina> listarFaltas(int idOficina, int idDia) {
        Conexao conexao = SingletonDB.conectar();
        return presencaModel.consultarPorOficinaEDia(idOficina, idDia, conexao);
    }
}

package com.example.proj_bga.model;

import com.example.proj_bga.dao.AssociaAlunoOficinaDAO;
import com.example.proj_bga.util.Conexao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssociaAlunoOficina {

    @Autowired
    private AssociaAlunoOficinaDAO dao;

    private int alu_id;
    private int ofc_id;

    public AssociaAlunoOficina(int alu_id, int ofc_id) {
        this.alu_id = alu_id;
        this.ofc_id = ofc_id;
    }

    public AssociaAlunoOficina(int alu_id) {
        this.alu_id = alu_id;
    }

    public AssociaAlunoOficina() {
    }

    public int getAlu_id() {
        return alu_id;
    }

    public void setAlu_id(int alu_id) {
        this.alu_id = alu_id;
    }

    public int getOfc_id() {
        return ofc_id;
    }

    public void setOfc_id(int ofc_id) {
        this.ofc_id = ofc_id;
    }

    public AssociaAlunoOficina gravaAlunoOficina(AssociaAlunoOficina associaAlunoOficina, Conexao conexao) {
        return dao.gravar(associaAlunoOficina, conexao);
    }

    public boolean excluiAlunoOficina(AssociaAlunoOficina associaAlunoOficina,  Conexao conexao) {
        return dao.excluir(associaAlunoOficina, conexao);
    }

    public List<AssociaAlunoOficina> consultarAlunosOficinas(String filtro, Conexao conexao) {
        return dao.get(filtro, conexao);
    }

    public AssociaAlunoOficina consultaAlunosOficinasID(int alu_id, Conexao conexao) {
        return dao.get(alu_id, conexao);
    }
}
package com.example.proj_bga.model;

import com.example.proj_bga.dao.DiasMarcadosDAO;
import com.example.proj_bga.util.Conexao;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public class DiasMarcados {
    private DiasMarcadosDAO dao = new DiasMarcadosDAO();
    private int id;
    private LocalDate data;
    private int idOFc;

    public DiasMarcados() {}

    public DiasMarcados(int id, LocalDate data, int idOFc) {
        this.id = id;
        this.data = data;
        this.idOFc = idOFc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public int getIdOFc() {
        return idOFc;
    }

    public void setIdOFc(int idOFc) {
        this.idOFc = idOFc;
    }

    public List<DiasMarcados> consultar(String filtro, Conexao conexao) {
        return this.dao.get(filtro, conexao);
    }

    public DiasMarcados consultarID(int id, Conexao conexao){ return dao.get(id, conexao);}

    public boolean deletar(DiasMarcados dias, Conexao conexao) {
        return dao.excluir(dias, conexao);
    }


}

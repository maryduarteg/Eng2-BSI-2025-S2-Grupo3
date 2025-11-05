package com.example.proj_bga.model;

import com.example.proj_bga.dao.OfertaOficinaDAO;
import com.example.proj_bga.util.Conexao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class OfertaOficina {
    @Autowired
    private OfertaOficinaDAO dao;

    private int id;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private Date dataInicio;
    private Date dataFim;
    private int professor;
    private char ativo;
    private int ofc_fk;

    public OfertaOficina(){}

    public OfertaOficina(int id,LocalTime horaInicio, LocalTime horaFim, Date dataInicio, Date dataFim, int professor, char ativo, int ofc_fk) {
        this.id = id;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.professor = professor;
        this.ativo = ativo;
        this.ofc_fk = ofc_fk;
    }

    public int getOfc_fk() {
        return ofc_fk;
    }

    public void setOfc_fk(int ofc_fk) {
        this.ofc_fk = ofc_fk;
    }

    public OfertaOficina(LocalTime horaInicio, LocalTime horaFim, Date dataInicio, Date dataFim, int professor, char ativo, int ofc_fk) {

        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.professor = professor;
        this.ativo = ativo;
        this.ofc_fk = ofc_fk;
    }

    public OfertaOficina(int ofcCod) {
        id = ofcCod;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraTermino() {
        return horaFim;
    }

    public void setHoraTermino(LocalTime horaFim) {
        this.horaFim = horaFim;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public int getProfessor() {
        return professor;
    }

    public void setProfessor(int professor) {
        this.professor = professor;
    }

    public char getAtivo() {
        return ativo;
    }

    public void setAtivo(char ativo) {
        this.ativo = ativo;
    }

    public List<OfertaOficina> consultarOficinas(String filtro, Conexao conexao){
        return dao.get(filtro, conexao);
    }
    public OfertaOficina consultarOficinasID(int id, Conexao conexao){
        return dao.get(id, conexao);
    }

    public OfertaOficina gravarOficina(OfertaOficina ofertaOficina, Conexao conexao) {
        return dao.gravar(ofertaOficina, conexao);
    }

//    public boolean deletarOficina(Oficina oficina) { return dao.excluir(oficina); }

    public boolean inativarOficina(int id, Conexao conexao) {
        return dao.inativarOficina(id, conexao);
    }

    public OfertaOficina alterarOficina(OfertaOficina ofertaOficina, Conexao conexao) { return dao.alterar(ofertaOficina, conexao); }

    public boolean verificarConflitoHorario(int professorId, Date dataInicio, Date dataFim, LocalTime horaInicio, LocalTime horaFim, Conexao conexao) {
        return dao.existeConflitoDeHorario(professorId, dataInicio, dataFim, horaInicio, horaFim, conexao);
    }

    public List<Map<String, Object>> listarProfessores(Conexao conexao) {
        return dao.listarProfessores(conexao);
    }

}

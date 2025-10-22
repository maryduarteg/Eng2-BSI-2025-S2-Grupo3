package com.example.proj_bga.model;

import com.example.proj_bga.dao.OficinaDAO;
import com.example.proj_bga.util.Conexao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
@Repository
public class Oficina {
    @Autowired
    private OficinaDAO dao;

    private int id;
    private String nome;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private Date dataInicio;
    private Date dataFim;
    private int professor;
    private char ativo;

    public Oficina(){}

    public Oficina(int id, String nome, LocalTime horaInicio, LocalTime horaFim, Date dataInicio, Date dataFim, int professor, char ativo) {
        this.id = id;
        this.nome = nome;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.professor = professor;
        this.ativo = ativo;
    }

    public Oficina(String nome, LocalTime horaInicio, LocalTime horaFim, Date dataInicio, Date dataFim, int professor, char ativo) {
        this.nome = nome;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.professor = professor;
        this.ativo = ativo;
    }

    public Oficina(int ofcCod, String ofcDesc) {
        nome = ofcDesc;
        id = ofcCod;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public List<Oficina> consultarOficinas(String filtro, Conexao conexao){
        return dao.get(filtro);
    }
    public Oficina consultarOficinasID(int id){
        return dao.get(id);
    }

    public Oficina gravarOficina(Oficina oficina) {
        return dao.gravar(oficina);
    }

    public boolean deletarOficina(Oficina oficina) { return dao.excluir(oficina); }

    public Oficina alterarOficina(Oficina oficina) { return dao.alterar(oficina); }
}

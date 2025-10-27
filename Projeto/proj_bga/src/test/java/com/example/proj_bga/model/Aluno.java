package com.example.proj_bga.model;

import com.example.proj_bga.dao.AlunoDAO;
import com.example.proj_bga.util.Conexao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class Aluno {
    @Autowired
    private AlunoDAO dao;
    private int id;
    private LocalDate dt_entrada;
    private String foto;
    private String mae;
    private String pai;
    private char responsavel_pais;
    private char conhecimento;
    private char pais_convivem;
    private char pensao;
    private int pes_id;

    public Aluno(int id, LocalDate dt_entrada, String foto,
                 String mae, String pai, char responsavel_pais,
                 char conhecimento, char pais_convivem,
                 char pensao, int pes_id)
    {
        this.id = id;
        this.dt_entrada = dt_entrada;
        this.foto = foto;
        this.mae = mae;
        this.pai = pai;
        this.responsavel_pais = responsavel_pais;
        this.conhecimento = conhecimento;
        this.pais_convivem = pais_convivem;
        this.pensao = pensao;
        this.pes_id = pes_id;
    }

    public Aluno() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDt_entrada() {
        return dt_entrada;
    }

    public void setDt_entrada(LocalDate dt_entrada) {
        this.dt_entrada = dt_entrada;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getMae() {
        return mae;
    }

    public void setMae(String mae) {
        this.mae = mae;
    }

    public String getPai() {
        return pai;
    }

    public void setPai(String pai) {
        this.pai = pai;
    }

    public char getResponsavel_pais() {
        return responsavel_pais;
    }

    public void setResponsavel_pais(char responsavel_pais) {
        this.responsavel_pais = responsavel_pais;
    }

    public char getConhecimento() {
        return conhecimento;
    }

    public void setConhecimento(char conhecimento) {
        this.conhecimento = conhecimento;
    }

    public char getPais_convivem() {
        return pais_convivem;
    }

    public void setPais_convivem(char pais_convivem) {
        this.pais_convivem = pais_convivem;
    }

    public char getPensao() {
        return pensao;
    }

    public void setPensao(char pensao) {
        this.pensao = pensao;
    }

    public int getPes_id() {
        return pes_id;
    }

    public void setPes_id(int pes_id) {
        this.pes_id = pes_id;
    }

    @Override
    public String toString() {
        return "Aluno{" +
                "dao=" + dao +
                ", id=" + id +
                ", dt_entrada=" + dt_entrada +
                ", foto='" + foto + '\'' +
                ", mae='" + mae + '\'' +
                ", pai='" + pai + '\'' +
                ", responsavel_pais=" + responsavel_pais +
                ", conhecimento=" + conhecimento +
                ", pais_convivem=" + pais_convivem +
                ", pensao=" + pensao +
                ", pes_id=" + pes_id +
                '}';
    }

    public List<Aluno> consultar(String filtro, Conexao conexao) {
        return this.dao.get(filtro);
    }

    public Aluno consultar(String filtro){
        return (Aluno) dao.get(filtro);
    }

    public Aluno consultar(int id){
        return dao.get(id);
    }

    public Aluno gravar(Aluno aluno) {
        return (Aluno) this.dao.gravar(aluno);
    }

    public boolean deletarAluno(Aluno aluno) {
        return dao.excluir(aluno);
    }

    public Aluno update(Aluno aluno)
    {
        return dao.alterar(aluno);
    }

    public boolean consultarAtivo(int id){return dao.verificarAtivoExistente(id);}
}

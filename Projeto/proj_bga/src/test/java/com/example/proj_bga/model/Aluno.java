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
    private String responsavel_pais;
    private String conhecimento;
    private String pais_convivem;
    private String pensao;
    private int pes_id;

    public Aluno(int id, LocalDate dt_entrada, String foto,
                 String mae, String pai, String responsavel_pais,
                 String conhecimento, String pais_convivem,
                 String pensao, int pes_id)
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

    public String getResponsavel_pais() {
        return responsavel_pais;
    }

    public void setResponsavel_pais(String responsavel_pais) {
        this.responsavel_pais = responsavel_pais;
    }

    public String getConhecimento() {
        return conhecimento;
    }

    public void setConhecimento(String conhecimento) {
        this.conhecimento = conhecimento;
    }

    public String getPais_convivem() {
        return pais_convivem;
    }

    public void setPais_convivem(String pais_convivem) {
        this.pais_convivem = pais_convivem;
    }

    public String getPensao() {
        return pensao;
    }

    public void setPensao(String pensao) {
        this.pensao = pensao;
    }

    public int getPes_id() {
        return pes_id;
    }

    public void setPes_id(int pes_id) {
        this.pes_id = pes_id;
    }

    public List<Aluno> consultar(String filtro, Conexao conexao) {
        return this.dao.get(filtro);
    }

    public Aluno consultar(String filtro){
        return (Aluno) dao.get(filtro);
    }

    public Aluno gravar(Aluno aluno) {
        return (Aluno) this.dao.gravar(aluno);
    }

    public boolean deletarAluno(Aluno aluno) {
        return dao.excluir(aluno);
    }

    public Aluno update(Aluno aluno)
    {
        return (Aluno) dao.alterar(aluno);
    }
}

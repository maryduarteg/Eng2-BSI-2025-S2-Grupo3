package proj_mvc.model;

import java.time.LocalTime;
import java.util.Date;

public class Oficina {

    private int id;
    private String nome;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private Date  dataInicio;
    private Date dataFim;
    private int professor;
    private char ativo;

    public Oficina() {}

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
}

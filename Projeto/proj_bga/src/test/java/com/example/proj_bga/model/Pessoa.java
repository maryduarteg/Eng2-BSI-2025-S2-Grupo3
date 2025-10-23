package com.example.proj_bga.model;

import java.time.LocalDate;
import java.util.InputMismatchException;


public class Pessoa {
    private int id;
    private String nome;
    private String cpf;
    private LocalDate dt_nascimento;
    private String rg;
    private char ativo;
    private int end_id;

    public Pessoa() {}

    public Pessoa(int id, String nome, String cpf, LocalDate dt_nascimento,
                  String rg, char ativo, int end_id) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.dt_nascimento = dt_nascimento;
        this.rg = rg;
        this.ativo = ativo;
        this.end_id = end_id;
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

    public String getcpf() {
        return this.cpf;
    }

    public void setcpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDt_nascimento() {
        return dt_nascimento;
    }

    public void setDt_nascimento(LocalDate dt_nascimento) {
        this.dt_nascimento = dt_nascimento;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public char getAtivo() {
        return ativo;
    }

    public void setAtivo(char ativo) {
        this.ativo = ativo;
    }

    public int getEnd_id() {
        return end_id;
    }

    public void setEnd_id(int end_id) {
        this.end_id = end_id;
    }
    public boolean validCPF() {
        // considera-se erro this.cpf"s formados por uma sequencia de numeros iguais
        if (this.cpf.equals("00000000000") ||
                this.cpf.equals("11111111111") ||
                this.cpf.equals("22222222222") || this.cpf.equals("33333333333") ||
                this.cpf.equals("44444444444") || this.cpf.equals("55555555555") ||
                this.cpf.equals("66666666666") || this.cpf.equals("77777777777") ||
                this.cpf.equals("88888888888") || this.cpf.equals("99999999999") ||
                (this.cpf.length() != 11))
            return(false);

        char dig10, dig11;
        int sm, i, r, num, peso;


        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i=0; i<9; i++) {
                // converte o i-esimo caractere do this.cpf em um numero:
                // por exemplo, transforma o caractere "0" no inteiro 0
                // (48 eh a posicao de "0" na tabela ASCII)
                num = (int)(this.cpf.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = 0;
            else dig10 = (char)(r + 48); // converte no respectivo caractere numerico

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for(i=0; i<10; i++) {
                num = (int)(this.cpf.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = 0;
            else dig11 = (char)(r + 48);

            // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == this.cpf.charAt(9)) && (dig11 == this.cpf.charAt(10)))
                return(true);
            else return(false);
        } catch (InputMismatchException erro) {
            return(false);
        }
    }
}

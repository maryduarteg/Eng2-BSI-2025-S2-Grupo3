package com.example.proj_bga.model;

import org.springframework.stereotype.Service;

@Service
public class DiasOficina {
    private int idDia;
    private int idOfc;

    public int getIdDia() {
        return idDia;
    }

    public void setIdDia(int idDia) {
        this.idDia = idDia;
    }

    public int getIdOfc() {
        return idOfc;
    }

    public void setIdOfc(int idOfc) {
        this.idOfc = idOfc;
    }
}

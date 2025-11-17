package com.example.proj_bga.model;

import com.example.proj_bga.dao.FotosOficinaDAO;
import com.example.proj_bga.util.Conexao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class FotosOficina {
    @Autowired
    private FotosOficinaDAO dao;

    private int alu_id;  // ADICIONADO
    private int pso_id;  // ADICIONADO
    private int ofc_id;
    private int dmf_id;
    private int fto_numero;
    private String fto_nome_arquivo;
    private Date fto_data_upload;
    private String fto_descricao;
    private byte[] fto_foto;

    public FotosOficina(int ofc_id, int dmf_id, int fto_numero, String fto_nome_arquivo, Date fto_data_upload, String fto_descricao, byte[] fto_foto) {
        this.ofc_id = ofc_id;
        this.dmf_id = dmf_id;
        this.fto_numero = fto_numero;
        this.fto_nome_arquivo = fto_nome_arquivo;
        this.fto_data_upload = fto_data_upload;
        this.fto_descricao = fto_descricao;
        this.fto_foto = fto_foto;
    }

    public FotosOficina() {}

    // Getters e Setters
    public int getAlu_id() { return alu_id; }  // ADICIONADO
    public void setAlu_id(int alu_id) { this.alu_id = alu_id; }  // ADICIONADO

    public int getPso_id() { return pso_id; }  // ADICIONADO
    public void setPso_id(int pso_id) { this.pso_id = pso_id; }  // ADICIONADO

    public int getOfc_id() { return ofc_id; }
    public void setOfc_id(int ofc_id) { this.ofc_id = ofc_id; }

    public int getDmf_id() { return dmf_id; }
    public void setDmf_id(int dmf_id) { this.dmf_id = dmf_id; }

    public int getFto_numero() { return fto_numero; }
    public void setFto_numero(int fto_numero) { this.fto_numero = fto_numero; }

    public String getFto_nome_arquivo() { return fto_nome_arquivo; }
    public void setFto_nome_arquivo(String fto_nome_arquivo) { this.fto_nome_arquivo = fto_nome_arquivo; }

    public Date getFto_data_upload() { return fto_data_upload; }
    public void setFto_data_upload(Date fto_data_upload) { this.fto_data_upload = fto_data_upload; }

    public String getFto_descricao() { return fto_descricao; }
    public void setFto_descricao(String fto_descricao) { this.fto_descricao = fto_descricao; }

    public byte[] getFto_foto() { return fto_foto; }
    public void setFto_foto(byte[] fto_foto) { this.fto_foto = fto_foto; }

    public FotosOficina gravaFotosOficina(FotosOficina f, Conexao conexao) {
        return dao.gravar(f, conexao);
    }

    public boolean excluirFotosOficina(FotosOficina f, Conexao conexao) {
        return dao.excluir(f, conexao);
    }

    public List<FotosOficina> consultarFotosOficina(String filtro, Conexao conexao) {
        return dao.get(filtro, conexao);
    }

    public FotosOficina consultaFotoOficinaID(int alu_id, int ofc_id, int pso_id, int dmf_id, int fto_numero, Conexao conexao) {
        return dao.get(alu_id, ofc_id, pso_id, dmf_id, fto_numero, conexao);
    }
}

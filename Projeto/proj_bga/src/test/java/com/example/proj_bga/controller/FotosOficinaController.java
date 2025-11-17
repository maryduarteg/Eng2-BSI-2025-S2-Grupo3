package com.example.proj_bga.controller;

import com.example.proj_bga.model.FotosOficina;
import com.example.proj_bga.util.Conexao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class FotosOficinaController {
    @Autowired
    private FotosOficina fotosOficinaModel;

    public Map<String, Object> gravarFotosOficina(int ofc_id, int dmf_id, int fto_numero,
                                                  String fto_nome_arquivo, Date fto_data_upload,
                                                  String fto_descricao, byte[] fto_foto) {

        Conexao conexao = new Conexao();

        // Validação simplificada
        if(ofc_id == 0 || dmf_id == 0 || fto_numero == 0 ||
                fto_nome_arquivo == null || fto_nome_arquivo.isEmpty() ||
                fto_data_upload == null ||
                fto_descricao == null || fto_descricao.isEmpty() ||
                fto_foto == null || fto_foto.length == 0) {

            return Map.of("erro", "Dados inválidos para cadastro da foto!");
        }

        FotosOficina foto = new FotosOficina(ofc_id, dmf_id, fto_numero,
                fto_nome_arquivo, fto_data_upload,
                fto_descricao, fto_foto);

        FotosOficina gravada = fotosOficinaModel.gravaFotosOficina(foto, conexao);

        if(gravada != null){
            Map<String, Object> json = new HashMap<>();
            json.put("ofc_id", gravada.getOfc_id());
            json.put("dmf_id", gravada.getDmf_id());
            json.put("fto_numero", gravada.getFto_numero());
            json.put("fto_nome_arquivo", gravada.getFto_nome_arquivo());
            json.put("fto_data_upload", gravada.getFto_data_upload());
            json.put("fto_descricao", gravada.getFto_descricao());
            return json;
        }

        return Map.of("erro", "Houve um erro ao gravar no banco!");
    }

    public Map<String, Object> deletarFotosOficina(int ofc_id, int dmf_id, int fto_numero) {

        Conexao conexao = new Conexao();

        if(ofc_id == 0 || dmf_id == 0 || fto_numero == 0) {
            return Map.of("erro", "Dados inválidos para exclusão da foto!");
        }

        FotosOficina foto = new FotosOficina();
        foto.setOfc_id(ofc_id);
        foto.setDmf_id(dmf_id);
        foto.setFto_numero(fto_numero);

        boolean excluida = fotosOficinaModel.excluirFotosOficina(foto, conexao);

        if(excluida){
            return Map.of("mensagem", "Foto excluída com sucesso!");
        }

        return Map.of("erro", "Houve um erro ao excluir do banco!");
    }

    public List<Map<String, Object>> getFotosOficina() {
        Conexao conexao = new Conexao();
        String filtro = "";
        List<FotosOficina> fotos = fotosOficinaModel.consultarFotosOficina(filtro, conexao);

        List<Map<String, Object>> lista = new ArrayList<>();
        for (FotosOficina f : fotos) {
            Map<String, Object> json = new HashMap<>();
            json.put("ofc_id", f.getOfc_id());
            json.put("dmf_id", f.getDmf_id());
            json.put("fto_numero", f.getFto_numero());
            json.put("fto_nome_arquivo", f.getFto_nome_arquivo());
            json.put("fto_data_upload", f.getFto_data_upload());
            json.put("fto_descricao", f.getFto_descricao());
            json.put("fto_foto", f.getFto_foto());
            lista.add(json);
        }

        return lista;
    }

    public FotosOficina buscarFoto(int aluId, int ofcId, int psoId, int dmfId,
                                   int ftoNumero, Conexao conexao) {
        return fotosOficinaModel.consultaFotoOficinaID(aluId, ofcId, psoId, dmfId,
                ftoNumero, conexao);
    }

    public List<FotosOficina> buscarFotosPorPresenca(int aluId, int ofcId, int psoId,
                                                     int dmfId, Conexao conexao) {
        String filtro = "WHERE alu_id = " + aluId + " AND ofc_id = " + ofcId +
                " AND pso_id = " + psoId + " AND dmf_id = " + dmfId;
        return fotosOficinaModel.consultarFotosOficina(filtro, conexao);
    }
}

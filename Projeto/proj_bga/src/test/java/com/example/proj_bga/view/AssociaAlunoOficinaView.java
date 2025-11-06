package com.example.proj_bga.view;

import com.example.proj_bga.controller.AssociaAlunoOficinaController;
import com.example.proj_bga.controller.OficinaController;
import com.example.proj_bga.model.AssociaAlunoOficina;
import com.example.proj_bga.model.DiasOficina;
import com.example.proj_bga.model.OfertaOficina;
import com.example.proj_bga.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.openmbean.OpenMBeanConstructorInfo;
import java.util.Map;

@RestController
@RequestMapping("apis/associa-aluno-oficina")

public class AssociaAlunoOficinaView {
    @Autowired
    private AssociaAlunoOficinaController associaController;

    @PostMapping
    public ResponseEntity<Object> AddAlunoOficina(@RequestBody AssociaAlunoOficina ass) {
        Map<String, Object> json = associaController.addAlunoOficina(ass.getAlu_id(), ass.getOfc_id());
        if(json.get("erro") == null){
            return ResponseEntity.ok(new Mensagem("Oficina cadastrada com sucesso!!"));
        }
        return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }
}

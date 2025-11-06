package com.example.proj_bga.view;

import com.example.proj_bga.controller.AssociaAlunoOficinaController;
import com.example.proj_bga.controller.OficinaController;
import com.example.proj_bga.model.AssociaAlunoOficina;
import com.example.proj_bga.model.DiasOficina;
import com.example.proj_bga.model.OfertaOficina;
import com.example.proj_bga.util.Mensagem;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.openmbean.OpenMBeanConstructorInfo;
import java.util.List;
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
            return ResponseEntity.ok(new Mensagem("Aluno associado à oficina com sucesso!!"));
        }
        return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @GetMapping
    public ResponseEntity<Object> getAluOfc(){
        List<Map<String, Object>> lista = associaController.getAlunoOficina();

        if (lista != null && !lista.isEmpty()) {
            return ResponseEntity.ok(lista); // cod 200
        }
        else{
            return ResponseEntity.badRequest().body(new Mensagem("Não foram encontrados registros!!")); // cod 400
        }
    }

    @DeleteMapping("/aluno/{alu_id}/oficina/{ofc_id}")
    public ResponseEntity<Object> deleteAluOfc(@PathVariable("alu_id") int alu_id, @PathVariable("ofc_id") int ofc_id){
        Map<String, Object> json = associaController.deletarAlunoOficina(alu_id, ofc_id);

        if(json.get("erro") == null){
            return ResponseEntity.ok(new Mensagem(json.get("mensagem").toString()));
        }
        else{
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }
}

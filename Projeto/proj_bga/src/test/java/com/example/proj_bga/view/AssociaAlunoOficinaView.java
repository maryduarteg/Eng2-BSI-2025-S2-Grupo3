package com.example.proj_bga.view;

import com.example.proj_bga.controller.AlunoController;
import com.example.proj_bga.controller.AssociaAlunoOficinaController;
import com.example.proj_bga.controller.VerificaPeriodoAlunoController;
import com.example.proj_bga.model.AssociaAlunoOficina;
import com.example.proj_bga.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apis/associa-aluno-oficina")
@CrossOrigin(origins = "*")
public class AssociaAlunoOficinaView {

    @Autowired
    private VerificaPeriodoAlunoController verificaPeriodoAlunoController;

    @Autowired
    private AssociaAlunoOficinaController associaController;

    @Autowired
    private AlunoController alunoController;

    @PostMapping
    public ResponseEntity<?> AddAlunoOficina(@RequestBody AssociaAlunoOficina ass) {
        Map<String, Object> json = associaController.addAlunoOficina(ass.getAlu_id(), ass.getOfc_id());

        if(json.get("erro") == null){
            return ResponseEntity.ok(new Mensagem("Aluno associado à oficina com sucesso!!"));
        }
        return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAluOfc(){
        List<Map<String, Object>> lista = associaController.getAlunoOficina();

        if (lista == null) {
            lista = new ArrayList<>();
        }

        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/aluno/{alu_id}/oficina/{ofc_id}")
    public ResponseEntity<?> deleteAluOfc(@PathVariable("alu_id") int alu_id, @PathVariable("ofc_id") int ofc_id) {
        Map<String, Object> json = associaController.deletarAlunoOficina(alu_id, ofc_id);

        if(json.get("erro") == null){
            return ResponseEntity.ok(new Mensagem(json.get("mensagem").toString()));
        } else {
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }

    @GetMapping("/alunos")
    public ResponseEntity<?> getAlunos(@RequestParam(value = "filtro", defaultValue = "") String filtro) {
        List<Map<String, Object>> lista = alunoController.getAlunos(filtro);
        if (lista != null && !lista.isEmpty()) {
            return ResponseEntity.ok(lista);
        }
        return ResponseEntity.ok(new ArrayList<>());
    }

    @GetMapping("/oficinas")
    public ResponseEntity<?> getOficinas() {
        List<Map<String, Object>> lista = associaController.getOficinas();

        if (lista != null && !lista.isEmpty()) {
            return ResponseEntity.ok(lista);
        }
        return ResponseEntity.ok(new ArrayList<>());
    }

    @GetMapping("/alunos-disponiveis/{ofertaId}")
    public ResponseEntity<List<Map<String, Object>>> obterAlunosDisponiveisSemConflito(@PathVariable int ofertaId) {

        List<Map<String, Object>> alunos = verificaPeriodoAlunoController
                .getAlunosDisponiveisSemConflito(ofertaId);
        return ResponseEntity.ok(alunos);
    }

}

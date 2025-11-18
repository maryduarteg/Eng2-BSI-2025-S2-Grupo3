package com.example.proj_bga.view;

import com.example.proj_bga.controller.PresencaPasseioController;
import com.example.proj_bga.model.PresencaPasseio;
import com.example.proj_bga.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("apis/presenca/passeio")
public class PresencaPasseioView {

    @Autowired
    private PresencaPasseioController controller;

    @PostMapping
    public ResponseEntity<Object> registrarFalta(@RequestBody PresencaPasseio dto) {
        if (controller.registrarFalta(dto.getIdAluno(), dto.getIdDia()) != null)
            return ResponseEntity.ok(new Mensagem("Falta registrada com sucesso!"));
        return ResponseEntity.badRequest().body(new Mensagem("Erro ao registrar falta"));
    }

    @GetMapping("/datas")
    public ResponseEntity<Object> listarDatas() {
        return ResponseEntity.ok(controller.listarDatas());
    }

    @GetMapping("/alunos/{dmp_id}")
    public ResponseEntity<Object> listarAlunos(@PathVariable int dmp_id) {
        return ResponseEntity.ok(controller.listarAlunos(dmp_id));
    }

    @GetMapping("/chamada-feita/{dmp_id}")
    public ResponseEntity<Object> chamadaFeita(@PathVariable int dmp_id) {
        boolean feita = controller.chamadaFeita(dmp_id);
        return ResponseEntity.ok(Map.of("chamadaFeita", feita));
    }

    @DeleteMapping("/{alu_id}/{dmp_id}")
    public ResponseEntity<Object> excluirFalta(@PathVariable int alu_id, @PathVariable int dmp_id) {
        if (controller.excluirFalta(alu_id, dmp_id))
            return ResponseEntity.ok(Map.of("mensagem", "Falta removida com sucesso!"));
        else
            return ResponseEntity.badRequest().body(Map.of("erro", "Erro ao remover falta"));
    }
}

package com.example.proj_bga.view;

import com.example.proj_bga.controller.PresencaOficinaController;
import com.example.proj_bga.model.PresencaOficina;
import com.example.proj_bga.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("apis/presenca/oficina")
public class PresencaOficinaView {

    @Autowired
    private PresencaOficinaController controllerOficinaPresenca;

    @PostMapping
    public ResponseEntity<Object> registrarFalta(@RequestBody PresencaOficina dto) {
        if (controllerOficinaPresenca.registrarFalta(dto.getIdAluno(), dto.getIdDia()) != null)
            return ResponseEntity.ok(new Mensagem("Falta registrada com sucesso!"));
        return ResponseEntity.badRequest().body(new Mensagem("Erro ao registrar falta"));
    }

    @GetMapping("/datas")
    public ResponseEntity<Object> listarDatas() {
        return ResponseEntity.ok(controllerOficinaPresenca.listarDatas());
    }

    @GetMapping("/alunos/{dmf_id}")
    public ResponseEntity<Object> listarAlunos(@PathVariable int dmf_id) {
        return ResponseEntity.ok(controllerOficinaPresenca.listarAlunos(dmf_id));
    }

    @GetMapping("/chamada-feita/{dmf_id}")
    public ResponseEntity<Object> chamadaFeita(@PathVariable int dmf_id) {
        boolean feita = controllerOficinaPresenca.chamadaFeita(dmf_id);
        return ResponseEntity.ok(Map.of("chamadaFeita", feita));
    }

    @DeleteMapping("/{alu_id}/{dmf_id}")
    public ResponseEntity<Object> excluirFalta(@PathVariable int alu_id, @PathVariable int dmf_id) {
        if (controllerOficinaPresenca.excluirFalta(alu_id, dmf_id))
            return ResponseEntity.ok(Map.of("mensagem", "Falta removida com sucesso!"));
        else
            return ResponseEntity.badRequest().body(Map.of("erro", "Erro ao remover falta"));
    }


}

package com.example.proj_bga.view;

import com.example.proj_bga.controller.DiasMarcadosController;
import com.example.proj_bga.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("apis/dias")
public class DiasMarcadosView {
    @Autowired
    private DiasMarcadosController diaController;

    @GetMapping
    public ResponseEntity<Object> get() {
        List<Map<String, Object>> lista = diaController.getDias("");

        if (lista != null && !lista.isEmpty()) {
            return ResponseEntity.ok(lista); // HTTP 200 com lista
        } else {
            return ResponseEntity.badRequest()
                    .body(new Mensagem("Nenhum dia encontrado!!")); // HTTP 400
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAluno(@PathVariable("id") int id)
    {
        Map<String, Object> json = diaController.deletarDia(id);
        if(json.get("erro") == null){
            return ResponseEntity.ok(new Mensagem(json.get("mensagem").toString()));
        }
        else{
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }
}

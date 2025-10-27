package com.example.proj_bga.view;

import com.example.proj_bga.controller.PasseioController;
import com.example.proj_bga.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("apis/passeio")
public class PasseioView {
    @Autowired
    private PasseioController passeioController;

    @PostMapping
    public ResponseEntity<Object> addPasseio(@RequestParam("pde_descricao") String pde_descricao){
        Map<String, Object> json = passeioController.addPass(pde_descricao);
        if(json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Passeio cadastrado com sucesso!"));
        return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @GetMapping
    public ResponseEntity<Object> consultar() {
        List<Map<String, Object>> lista = passeioController.getPass();
        if (lista != null && !lista.isEmpty()) {
            return ResponseEntity.ok(lista);
        }
        return ResponseEntity.badRequest().body(new Mensagem("Nenhum passeio encontrado!!"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePasseio(@PathVariable("id") int id) {
        Map<String, Object> json = passeioController.deletarPasseio(id);
        if(json.get("erro") == null){
            return ResponseEntity.ok(new Mensagem(json.get("mensagem").toString()));
        }
        else{
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }

    @PutMapping
    public ResponseEntity<Object> updatePasseio(@RequestParam("pde_id") int pde_id, @RequestParam("pde_descricao") String pde_descricao){
        Map<String, Object> json = passeioController.updatePasseio(pde_id, pde_descricao);
        if(json.containsKey("erro")) {
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
        else
            if (json.containsKey("mensagem")) {
                return ResponseEntity.ok(new Mensagem(json.get("mensagem").toString()));
            }
            else {
                return ResponseEntity.ok(new Mensagem("Atualização realizada com sucesso."));
            }
    }
}

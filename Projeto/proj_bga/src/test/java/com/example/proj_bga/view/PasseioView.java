package com.example.proj_bga.view;

import com.example.proj_bga.controller.PasseioController;
import com.example.proj_bga.model.Passeio;
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
    public ResponseEntity<Object> addPasseio(@RequestBody Passeio  passeio) {
        System.out.println(passeio);
        Map<String, Object> json = passeioController.addPass(
                passeio.getDescricao()
        );

        if(json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Passeio cadastrado com sucesso!"));
        return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @GetMapping
    public ResponseEntity<Object> consultar(@RequestParam(value = "filtro", required = false) String filtro) {

        if (filtro == null) {
            filtro = "";
        }
        List<Map<String, Object>> lista = passeioController.getPassFiltro(filtro);
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
    public ResponseEntity<Object> updatePasseio(@RequestBody Passeio passeio){
        Map<String, Object> json = passeioController.updatePasseio(
                passeio.getPdeId(),
                passeio.getDescricao()
        );

        if (json.get("erro") == null) {
            return ResponseEntity.ok(new Mensagem(json.containsKey("mensagem")? json.get("mensagem").toString(): "Passeio atualizado com sucesso!"));
        }
        else {
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }
}

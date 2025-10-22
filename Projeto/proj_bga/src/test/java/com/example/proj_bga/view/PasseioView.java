package com.example.proj_bga.view;

import com.example.proj_bga.controller.PasseioController;
import com.example.proj_bga.model.Passeio;
import com.example.proj_bga.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("apis/passeio")
public class PasseioView {
    @Autowired
    private PasseioController passeioController;

    @PostMapping
    public ResponseEntity<Object> addPasseio(
            @RequestParam("pas_data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date data,
            @RequestParam("pas_hora_inicio") LocalTime hora_inicio,
            @RequestParam("pas_hora_final") LocalTime hora_final,
            @RequestParam("pas_chamada_feita") String chamada_feita,
            @RequestParam("pde_id") int pde){

        Map<String, Object> json = passeioController.addPass(data, hora_inicio, hora_final, chamada_feita, pde);
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
    public ResponseEntity<Object> updatePasseio(
            @RequestParam("pas_id") int id,
            @RequestParam("pas_data")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date data,
            @RequestParam("pas_hora_inicio") LocalTime hora_inicio,
            @RequestParam("pas_hora_final") LocalTime hora_final,
            @RequestParam("pas_chamada_feita") String chamada_feita,
            @RequestParam("pde_id") int pde){


        Map<String, Object> json = passeioController.updatePasseio(id, data, hora_inicio, hora_final, chamada_feita, pde, pde);

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

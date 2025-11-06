package com.example.proj_bga.view;
import com.example.proj_bga.controller.OficinaController;
import com.example.proj_bga.model.OfertaOficina;
import com.example.proj_bga.model.Oficina;
import com.example.proj_bga.util.Conexao;
import com.example.proj_bga.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("apis/oficina")
public class OficinaView {

    @Autowired
    private OficinaController oficinaController;
    private ConversionService conversionService;

    @PostMapping
    public ResponseEntity<Object> addOficinas(@RequestBody Oficina dto) {
        Map<String, Object> json = oficinaController.addOficina(
                dto.getDescricao()
        );

        if(json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Oficina cadastrada com sucesso!!"));

        return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }



    @GetMapping
    public ResponseEntity<Object> get() {
        List<Map<String, Object>> lista = oficinaController.getOficina();

        if (lista != null && !lista.isEmpty()) {
            return ResponseEntity.ok(lista); // HTTP 200 com lista
        } else {
            return ResponseEntity.badRequest()
                    .body(new Mensagem("Nenhuma oficina encontrada!!")); // HTTP 400
        }
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Object> getOficinaId(@PathVariable int id, Conexao conexao) {
        Map<String, Object> oficina = oficinaController.getOficinaPorId(id, conexao);

        if (oficina != null) {
            return ResponseEntity.ok(oficina); // HTTP 200 com JSON da oficina
        } else {
            return ResponseEntity.badRequest()
                    .body(new Mensagem("Oficina não encontrada!")); // HTTP 400
        }
    }


    @PutMapping
    public ResponseEntity<Object> updateOficinas(@RequestBody Oficina dto) {
        Map<String, Object> json = oficinaController.updateOficina(
                dto.getId(),
                dto.getDescricao(),
                dto.getAtivo()
        );

        if(json.get("erro") == null) {
            return ResponseEntity.ok(new Mensagem("Oficina atualizada com sucesso!"));
        } else {
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }


}

package com.example.proj_bga.view;

import com.example.proj_bga.controller.OficinaController;
import com.example.proj_bga.model.Oficina;
import com.example.proj_bga.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("apis/oficina")
public class OficinaView {

    @Autowired
    private OficinaController oficinaController;

    @PostMapping
    public ResponseEntity<Object> addOficinas(@RequestParam("id")int id,
                                              @RequestParam("Nome")String nome,
                                              @RequestParam("Hora_Inicio") LocalTime horaInicio,
                                              @RequestParam("Hora_Fim")LocalTime horaFim,
                                              @RequestParam("Data_Inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dataInicio,
                                              @RequestParam("Data_Fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dataFim,
                                              @RequestParam("Professor") int professor,
                                              @RequestParam("Ativo") char ativo) {
        Map<String, Object> json = oficinaController.addOficina(nome, horaInicio, horaFim, dataInicio, dataFim, professor, ativo);
        if(json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Passeio cadastrado com sucesso!"));
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

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOficinaID(@PathVariable("id") int id) {
        Map<String, Object> oficina = oficinaController.getOficinaPorId(id);

        if (oficina != null) {
            return ResponseEntity.ok(oficina); // HTTP 200 com JSON da oficina
        } else {
            return ResponseEntity.badRequest()
                    .body(new Mensagem("Oficina não encontrada!")); // HTTP 400
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOficina(@PathVariable("id") int id) {
        Map<String, Object> json = oficinaController.deletarOficina(id);
        if(json.get("erro") == null){
            return ResponseEntity.ok(new Mensagem(json.get("mensagem").toString()));
        }
        else{
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }

    @PutMapping
    public ResponseEntity<Object> updateOficinas(
            @RequestParam("id") int id,
            @RequestParam("Nome") String nome,
            @RequestParam("Hora_Inicio") LocalTime horaInicio,
            @RequestParam("Hora_Fim") LocalTime horaFim,
            @RequestParam("Data_Inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dataInicio,
            @RequestParam("Data_Fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dataFim,
            @RequestParam("Professor") int professor,
            @RequestParam("Ativo") char ativo) {

        Map<String, Object> json = oficinaController.updateOficina(id, nome, horaInicio, horaFim, dataInicio, dataFim, professor, ativo);

        if (json.get("erro") == null) {
            return ResponseEntity.ok(new Mensagem(
                    json.containsKey("mensagem")
                            ? json.get("mensagem").toString()
                            : "Oficina atualizada com sucesso!"
            ));
        } else {
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }





}

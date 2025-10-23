package com.example.proj_bga.view;

import com.example.proj_bga.controller.AlunoController;
import com.example.proj_bga.model.Aluno;
import com.example.proj_bga.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("apis/aluno")
public class AlunoView {
    @Autowired
    private AlunoController alunoController;

    @PostMapping
    public ResponseEntity<Object> addAlunos(
            @RequestParam("dt_entrada") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dt_entrada,
            @RequestParam("foto") String foto,
            @RequestParam("mae") String mae,
            @RequestParam("pai") String pai,
            @RequestParam("responsavel_pais") char responsavel_pais,
            @RequestParam("conhecimento") char conhecimento,
            @RequestParam("pais_convivem") char pais_convivem,
            @RequestParam("pensao") char pensao,
            @RequestParam("pes_id") char pes_id
            ){
        Map<String, Object> json = alunoController.addAluno(dt_entrada,foto,mae,pai,responsavel_pais,conhecimento,pais_convivem,pensao,pes_id);
        if (json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Aluno cadastrado com sucesso!"));
        return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOficina(@PathVariable("id") int id) {
        Map<String, Object> json = alunoController.deletarAluno(id);
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
            @RequestParam("dt_entrada") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dt_entrada,
            @RequestParam("foto") String foto,
            @RequestParam("mae") String mae,
            @RequestParam("pai") String pai,
            @RequestParam("responsavel_pais") char responsavel_pais,
            @RequestParam("conhecimento") char conhecimento,
            @RequestParam("pais_convivem") char pais_convivem,
            @RequestParam("pensao") char pensao,
            @RequestParam("pes_id") char pes_id
    ) {

        Map<String, Object> json = alunoController.atualizarAluno(id, dt_entrada, foto, mae, pai, responsavel_pais, conhecimento, pais_convivem, pensao, pes_id);

        if (json.get("erro") == null) {
            return ResponseEntity.ok(new Mensagem(
                    json.containsKey("mensagem")
                            ? json.get("mensagem").toString()
                            : "Aluno atualizada com sucesso!"
            ));
        } else {
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }

    @GetMapping
    public ResponseEntity<Object> get() {
        List<Map<String, Object>> lista = alunoController.getAlunos("");

        if (lista != null && !lista.isEmpty()) {
            return ResponseEntity.ok(lista); // HTTP 200 com lista
        } else {
            return ResponseEntity.badRequest()
                    .body(new Mensagem("Nenhum aluno encontrado!!")); // HTTP 400
        }
    }
    //.




}

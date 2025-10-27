package com.example.proj_bga.view;

import com.example.proj_bga.controller.AlunoController;
import com.example.proj_bga.model.Aluno;
import com.example.proj_bga.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("apis/aluno")
public class AlunoView {
    @Autowired
    private AlunoController alunoController;

    @PostMapping
    public ResponseEntity<Object> addAlunos(@RequestBody Aluno aluno) {
        System.out.println(aluno);
        Map<String, Object> json = alunoController.addAluno(
                aluno.getDt_entrada(),
                aluno.getFoto(),
                aluno.getMae(),
                aluno.getPai(),
                aluno.getResponsavel_pais(),
                aluno.getConhecimento(),
                aluno.getPais_convivem(),
                aluno.getPensao(),
                aluno.getPes_id()
        );

        if (json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Aluno cadastrado com sucesso!"));
        return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAluno(@PathVariable("id") int id)
    {
        Map<String, Object> json = alunoController.deletarAluno(id);
        if(json.get("erro") == null){
            return ResponseEntity.ok(new Mensagem(json.get("mensagem").toString()));
        }
        else{
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }

    @PutMapping
    public ResponseEntity<Object> updateAluno(@RequestBody Aluno aluno) {

        Map<String, Object> json = alunoController.atualizarAluno(
                aluno.getId(),
                aluno.getDt_entrada(),
                aluno.getFoto(),
                aluno.getMae(),
                aluno.getPai(),
                aluno.getResponsavel_pais(),
                aluno.getConhecimento(),
                aluno.getPais_convivem(),
                aluno.getPensao(),
                aluno.getPes_id()
        );

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
    @GetMapping("/{id}")
    public ResponseEntity<Object> getAlunoID(@PathVariable("id") int id) {
        List<Map<String, Object>> aluno = alunoController.getAlunos("WHERE ALU_ID = "+id);

        if (aluno != null)
            return ResponseEntity.ok(aluno); // HTTP 200 com JSON da oficina
        else {
            return ResponseEntity.badRequest()
                    .body(new Mensagem("Aluno não encontrado!")); // HTTP 400
        }
    }
}

package com.example.proj_bga.view;

import com.example.proj_bga.controller.AlunoController;
import com.example.proj_bga.controller.ProfessorController;
import com.example.proj_bga.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("apis/professor")

public class ProfessorView {
    @Autowired
    private ProfessorController professorController;
    @GetMapping
    public ResponseEntity<Object> get() {
        List<Map<String, Object>> lista = professorController.getProfessores("");

        if (lista != null && !lista.isEmpty()) {
            return ResponseEntity.ok(lista); // HTTP 200 com lista
        } else {
            return ResponseEntity.badRequest()
                    .body(new Mensagem("Nenhum aluno encontrado!!")); // HTTP 400
        }
    }
}

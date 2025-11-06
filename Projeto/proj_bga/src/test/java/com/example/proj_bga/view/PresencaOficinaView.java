package com.example.proj_bga.view;

import com.example.proj_bga.controller.PresencaOficinaController;
import com.example.proj_bga.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("apis/presenca/oficina")
public class PresencaOficinaView {

    @Autowired
    private PresencaOficinaController controller;

    // Registrar falta
    @PostMapping
    public ResponseEntity<Object> registrarFalta(@RequestParam int idAluno,
                                                 @RequestParam int idOficina,
                                                 @RequestParam int idDia) {
        if (controller.registrarFalta(idAluno, idOficina, idDia) != null) {
            return ResponseEntity.ok(new Mensagem("Falta registrada com sucesso!"));
        }
        return ResponseEntity.badRequest().body(new Mensagem("Erro ao registrar falta"));
    }

    // Listar faltas por oficina e dia
    @GetMapping
    public ResponseEntity<Object> listarFaltas(@RequestParam int idOficina,
                                               @RequestParam int idDia) {
        List<?> lista = controller.listarFaltas(idOficina, idDia);
        if (lista != null && !lista.isEmpty())
            return ResponseEntity.ok(lista);
        return ResponseEntity.badRequest().body(new Mensagem("Nenhuma falta registrada"));
    }
}

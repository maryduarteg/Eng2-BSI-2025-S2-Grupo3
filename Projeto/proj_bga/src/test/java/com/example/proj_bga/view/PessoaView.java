package com.example.proj_bga.view;


import com.example.proj_bga.controller.PessoaController;
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
@RequestMapping("apis/pessoa")
public class PessoaView {
    @Autowired
    private PessoaController pessoaController;
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> get() {
        List<Map<String, Object>> lista = pessoaController.getAll();

        if (lista != null && !lista.isEmpty()) {
            return ResponseEntity.ok(lista);
        } else {
            return ResponseEntity.badRequest()
                    .body(List.of(Map.of("mensagem", "Nenhuma pessoa encontrada!!")));
        }
    }

}

package com.example.proj_bga.view;


import com.example.proj_bga.controller.PessoaController;
import com.example.proj_bga.model.Pessoa;
import com.example.proj_bga.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping("alunos")
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAlunos() {
        List<Map<String, Object>> lista = pessoaController.getAlunos();

        if (lista != null && !lista.isEmpty()) {
            return ResponseEntity.ok(lista);
        } else {
            return ResponseEntity.badRequest()
                    .body(List.of(Map.of("mensagem", "Nenhuma pessoa encontrada!!")));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getPessoaID(@PathVariable("id") int id) {
        Map<String, Object> pessoa = pessoaController.getByID(id);

        if (pessoa != null)
            return ResponseEntity.ok(pessoa); // HTTP 200 com JSON da oficina
        else {
            return ResponseEntity.badRequest()
                    .body(new Mensagem("Pessoa não encontrada!")); // HTTP 400
        }
    }

    @PutMapping
    public ResponseEntity<Object> updatePessoa(@RequestBody Pessoa pessoa) {

        Map<String, Object> json = pessoaController.atualizarPessoa(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getcpf(),
                pessoa.getDt_nascimento(),
                pessoa.getRg(),
                pessoa.getAtivo(),
                pessoa.getEnd_id()
        );

        if (json.get("erro") == null) {
            return ResponseEntity.ok(new Mensagem(
                    json.containsKey("mensagem")
                            ? json.get("mensagem").toString()
                            : "pessoa atualizada com sucesso!"
            ));
        } else {
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }

}

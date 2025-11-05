package com.example.proj_bga.view;

import com.example.proj_bga.controller.UsuarioController;
import com.example.proj_bga.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("apis/usuario")
public class UsuarioView {
    @Autowired
    private UsuarioController usuarioController;

    @PostMapping
    public ResponseEntity<Object> logar(@RequestBody Map<String, String> dados)
    {
        String usuario = dados.get("usuario");
        String senha = dados.get("senha");

        Map<String, Object> json = usuarioController.logar(usuario, senha);
        if(json.get("erro") == null)
            return ResponseEntity.ok(json);

        //Verificar como mandar este erro:
        return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }
}

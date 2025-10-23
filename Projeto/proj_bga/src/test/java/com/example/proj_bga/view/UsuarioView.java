package com.example.proj_bga.view;

import com.example.proj_bga.controller.OficinaController;
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
         /*
        if(usuario.equals("Gabriel") && senha.equals("Teste@123"))
            return ResponseEntity.ok(true);*/

        return ResponseEntity.ok(usuarioController.logar(usuario, senha));
        /*boolean json = usuarioController.logar(usuario, senha);
        if(json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Passeio cadastrado com sucesso!"));
        return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));*/
    }
}

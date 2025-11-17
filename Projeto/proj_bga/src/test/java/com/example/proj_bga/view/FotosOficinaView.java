package com.example.proj_bga.view;

import com.example.proj_bga.controller.FotosOficinaController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;  // ADICIONADO

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/apis/fotos/oficina")
public class FotosOficinaView {
    @Autowired
    private FotosOficinaController fotosOficinaController;

    // Listar todas as fotos
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listarTodasFotos() {
        List<Map<String, Object>> fotos = fotosOficinaController.getFotosOficina();
        if (fotos == null) {
            fotos = new ArrayList<>();
        }

        // Converter byte[] para Base64
        for (Map<String, Object> foto : fotos) {
            if (foto.get("fto_foto") != null) {
                byte[] bytes = (byte[]) foto.get("fto_foto");
                String base64 = Base64.getEncoder().encodeToString(bytes);
                foto.put("fto_foto", base64);
            }
        }
        return ResponseEntity.ok(fotos);
    }

    // CORRIGIDO - Buscar fotos por oficina (agora filtra corretamente)
    @GetMapping("/oficina/{ofc_id}")
    public ResponseEntity<List<Map<String, Object>>> buscarPorOficina(@PathVariable int ofc_id) {
        List<Map<String, Object>> fotos = fotosOficinaController.getFotosOficina();
        if (fotos == null) {
            fotos = new ArrayList<>();
        }

        // FILTRAR por ofc_id
        fotos = fotos.stream()
                .filter(foto -> {
                    Object id = foto.get("ofc_id");
                    return id != null && (Integer) id == ofc_id;
                })
                .collect(Collectors.toList());

        // Converter byte[] para Base64
        for (Map<String, Object> foto : fotos) {
            if (foto.get("fto_foto") != null) {
                byte[] bytes = (byte[]) foto.get("fto_foto");
                String base64 = Base64.getEncoder().encodeToString(bytes);
                foto.put("fto_foto", base64);
            }
        }
        return ResponseEntity.ok(fotos);
    }

    // Deletar foto
    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deletarFoto(
            @RequestParam int ofc_id,
            @RequestParam int dmf_id,
            @RequestParam int fto_numero) {
        System.out.println("=== DELETANDO FOTO ===");
        System.out.println("ofc_id: " + ofc_id);
        System.out.println("dmf_id: " + dmf_id);
        System.out.println("fto_numero: " + fto_numero);

        Map<String, Object> resultado = fotosOficinaController.deletarFotosOficina(
                ofc_id, dmf_id, fto_numero
        );

        if (resultado.containsKey("erro")) {
            return ResponseEntity.badRequest().body(resultado);
        }

        return ResponseEntity.ok(resultado);
    }
}

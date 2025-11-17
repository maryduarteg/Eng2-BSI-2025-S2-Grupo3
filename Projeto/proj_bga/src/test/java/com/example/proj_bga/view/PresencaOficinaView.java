package com.example.proj_bga.view;

import com.example.proj_bga.controller.FotosOficinaController;
import com.example.proj_bga.controller.PresencaOficinaController;
import com.example.proj_bga.model.PresencaOficina;
import com.example.proj_bga.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Date;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("apis/presenca/oficina")
public class PresencaOficinaView {
    @Autowired
    private PresencaOficinaController controllerOficinaPresenca;

    @Autowired
    private FotosOficinaController fotosOficinaController;

    @PostMapping
    public ResponseEntity registrarFalta(@RequestBody PresencaOficina dto) {
        if (controllerOficinaPresenca.registrarFalta(dto.getIdAluno(), dto.getIdDia()) != null)
            return ResponseEntity.ok(new Mensagem("Falta registrada com sucesso!"));
        return ResponseEntity.badRequest().body(new Mensagem("Erro ao registrar falta"));
    }  // CHAVE FALTANDO - ADICIONADA

    @PostMapping("/fotos")
    public ResponseEntity<Map<String, Object>> salvarFotosPresenca(
            @RequestParam(value = "files", required = false) MultipartFile[] files,
            @RequestParam int dmf_id,
            @RequestParam String fto_descricao) {
        System.out.println("=== INICIANDO UPLOAD DE FOTOS ===");
        System.out.println("dmf_id recebido: " + dmf_id);

        try {
            if (files == null || files.length == 0) {
                return ResponseEntity.ok(Map.of("mensagem", "Nenhuma foto anexada"));
            }

            if (files.length > 2) {
                return ResponseEntity.badRequest()
                        .body(Map.of("erro", "Máximo de 2 fotos permitidas"));
            }

            int ofc_id = controllerOficinaPresenca.buscarOficinaIdPorDia(dmf_id);
            System.out.println("ofc_id encontrado: " + ofc_id);
            if (ofc_id == 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("erro", "Oficina não encontrada para este dia"));
            }

            int fotosSalvas = 0;
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                if (!file.isEmpty()) {
                    Map<String, Object> resultado = fotosOficinaController.gravarFotosOficina(
                            ofc_id,
                            dmf_id,
                            i + 1,
                            file.getOriginalFilename(),
                            new Date(),
                            fto_descricao,
                            file.getBytes()
                    );
                    if (!resultado.containsKey("erro")) {
                        fotosSalvas++;
                    }
                }
            }
            return ResponseEntity.ok(Map.of(
                    "mensagem", fotosSalvas + " foto(s) salva(s) com sucesso!"
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("erro", "Erro ao processar fotos: " + e.getMessage()));
        }
    }  // CHAVE FALTANDO - ADICIONADA

    @GetMapping("/datas")
    public ResponseEntity listarDatas() {
        return ResponseEntity.ok(controllerOficinaPresenca.listarDatas());
    }

    @GetMapping("/alunos/{dmf_id}")
    public ResponseEntity listarAlunos(@PathVariable int dmf_id) {
        return ResponseEntity.ok(controllerOficinaPresenca.listarAlunos(dmf_id));
    }

    @GetMapping("/chamada-feita/{dmf_id}")
    public ResponseEntity chamadaFeita(@PathVariable int dmf_id) {
        boolean feita = controllerOficinaPresenca.chamadaFeita(dmf_id);
        return ResponseEntity.ok(Map.of("chamadaFeita", feita));
    }
}  // CHAVE FALTANDO - ADICIONADA

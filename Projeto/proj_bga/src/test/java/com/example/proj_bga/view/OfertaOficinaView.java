package com.example.proj_bga.view;

import com.example.proj_bga.controller.OfertarOficinaController;
import com.example.proj_bga.model.OfertaOficina;
import com.example.proj_bga.util.Conexao;
import com.example.proj_bga.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("apis/ofertaOficina")
public class OfertaOficinaView {

    @Autowired
    private OfertarOficinaController oficinaController;
    private ConversionService conversionService;

    @PostMapping
    public ResponseEntity<Object> addOficinas(@RequestBody OfertaOficina dto) {
        Map<String, Object> json = oficinaController.addOficina(
                dto.getHoraInicio(),
                dto.getHoraTermino(),
                dto.getDataInicio(),
                dto.getDataFim(),
                dto.getProfessor(),
                dto.getAtivo(),
                dto.getOfc_fk()

        );

        if(json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Oficina cadastrada com sucesso!!"));

        return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @GetMapping("/professores")
    public ResponseEntity<Object> getProfessores(Conexao conexao) {
        List<Map<String, Object>> lista = oficinaController.getProfessores(conexao);
        if (lista != null && !lista.isEmpty()) {
            return ResponseEntity.ok(lista);
        } else {
            return ResponseEntity.badRequest().body(new Mensagem("Nenhum professor encontrado!"));
        }
    }


    @GetMapping
    public ResponseEntity<Object> get() {
        List<Map<String, Object>> lista = oficinaController.getOficina();

        if (lista != null && !lista.isEmpty()) {
            return ResponseEntity.ok(lista); // HTTP 200 com lista
        } else {
            return ResponseEntity.badRequest()
                    .body(new Mensagem("Nenhuma oficina encontrada!!")); // HTTP 400
        }
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Object> getOficinaId(@PathVariable int id, Conexao conexao) {
        Map<String, Object> oficina = oficinaController.getOficinaPorId(id, conexao);

        if (oficina != null) {
            return ResponseEntity.ok(oficina); // HTTP 200 com JSON da oficina
        } else {
            return ResponseEntity.badRequest()
                    .body(new Mensagem("Oficina não encontrada!")); // HTTP 400
        }
    }



//    @DeleteMapping("/{id}")
//    public ResponseEntity<Object> deleteOficina(@PathVariable("id") int id) {
//        Map<String, Object> json = oficinaController.deletarOficina(id);
//        if(json.get("erro") == null){
//            return ResponseEntity.ok(new Mensagem(json.get("mensagem").toString()));
//        }
//        else{
//            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
//        }
//    }

    @PutMapping
    public ResponseEntity<Object> updateOficinas(
            @RequestParam("id") int id,
            @RequestParam("Hora_Inicio") LocalTime horaInicio,
            @RequestParam("Hora_Fim") LocalTime horaFim,
            @RequestParam("Data_Inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dataInicio,
            @RequestParam("Data_Fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dataFim,
            @RequestParam("Professor") int professor,
            @RequestParam("Ativo") char ativo,
            @RequestParam("ofc_pk") int ofc_pk) {

        Map<String, Object> json = oficinaController.updateOficina(id, horaInicio, horaFim, dataInicio, dataFim, professor, ativo, ofc_pk);

        if (json.get("erro") == null) {
            return ResponseEntity.ok(new Mensagem(
                    json.containsKey("mensagem")
                            ? json.get("mensagem").toString()
                            : "Oficina atualizada com sucesso!"
            ));
        } else {
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }

    @PutMapping("/inativar/{id}")
    public ResponseEntity<Object> inativarOficina(@PathVariable("id") int id) {
        Map<String, Object> json = oficinaController.inativarOficina(id); // chama o método que você já tem
        if(json.get("erro") == null){
            return ResponseEntity.ok(new Mensagem(json.get("mensagem").toString()));
        } else {
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }


    // Verificar conflito de horário
    @GetMapping("/verificar-conflito")
    public ResponseEntity<Map<String, Boolean>> verificarConflito(
            @RequestParam("professorId") int professorId,
            @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dataInicio,
            @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dataFim,
            @RequestParam("horaInicio") String horaInicio,
            @RequestParam("horaFim") String horaFim) {

        LocalTime horaInicioLT = LocalTime.parse(horaInicio);
        LocalTime horaFimLT = LocalTime.parse(horaFim);

        boolean conflito = oficinaController.existeConflitoDeHorario( professorId, dataInicio, dataFim, horaInicioLT, horaFimLT);
        Map<String, Boolean> resposta = new HashMap<>();
        resposta.put("existe", conflito);
        return ResponseEntity.ok(resposta);
    }




}

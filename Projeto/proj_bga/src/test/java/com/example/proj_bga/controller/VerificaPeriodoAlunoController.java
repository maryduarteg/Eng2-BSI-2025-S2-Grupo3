package com.example.proj_bga.controller;

import com.example.proj_bga.model.Aluno;
import com.example.proj_bga.model.AssociaAlunoOficina;
import com.example.proj_bga.model.OfertaOficina;
import com.example.proj_bga.model.Pessoa;
import com.example.proj_bga.util.Conexao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VerificaPeriodoAlunoController {

    @Autowired
    private Aluno alunoModel;

    @Autowired
    private OfertaOficina ofertaOficinaModel;

    @Autowired
    private AssociaAlunoOficina associaAlunoOficinaModel;

    @Autowired
    private Pessoa pessoaModel;

    private boolean horariosConflitam(LocalTime inicio1, LocalTime fim1, LocalTime inicio2, LocalTime fim2) {
        return (inicio1.isBefore(fim2) && fim1.isAfter(inicio2));
    }

    public List<Map<String, Object>> getAlunosDisponiveisSemConflito(int ofertaId) {
        Conexao conexao = new Conexao();

        List<Aluno> todosAlunos = alunoModel.consultar("", conexao);
        if (todosAlunos == null || todosAlunos.isEmpty())
            return new ArrayList<>();

        List<OfertaOficina> ofertas = ofertaOficinaModel.consultarOficinas("", conexao);
        OfertaOficina ofertaSelecionada = null;
        for (OfertaOficina oferta : ofertas) {
            if (oferta.getId() == ofertaId && oferta.getAtivo() == 'S') {
                ofertaSelecionada = oferta;
                break;
            }
        }
        if (ofertaSelecionada == null) {
            return new ArrayList<>();
        }

        LocalTime horaInicioOficina = ofertaSelecionada.getHoraInicio();
        LocalTime horaFimOficina = ofertaSelecionada.getHoraTermino();

        List<AssociaAlunoOficina> todosVinculos = associaAlunoOficinaModel.consultarAlunosOficinas("", conexao);
        List<Map<String, Object>> alunosDisponiveis = new ArrayList<>();

        for (Aluno aluno : todosAlunos) {
            int alunoId = aluno.getId();
            boolean temConflito = false;

            Pessoa pessoa = pessoaModel.getId(aluno.getPes_id(), conexao);
            if (pessoa == null || !String.valueOf(pessoa.getAtivo()).trim().equals("S")) {
                continue;
            }

            boolean jaVinculadoNaOferta = false;
            if (todosVinculos != null) {
                for (AssociaAlunoOficina vinculo : todosVinculos) {
                    if (vinculo.getAlu_id() == alunoId && vinculo.getOfc_id() == ofertaSelecionada.getId()) {
                        jaVinculadoNaOferta = true;
                        break;
                    }
                }
            }
            if (jaVinculadoNaOferta) {
                continue;
            }

            if (todosVinculos != null) {
                for (AssociaAlunoOficina vinculo : todosVinculos) {
                    if (vinculo.getAlu_id() == alunoId) {
                        for (OfertaOficina oferta : ofertas) {
                            if (oferta.getId() == vinculo.getOfc_id() && oferta.getAtivo() == 'S') {
                                LocalTime horaInicioVinculo = oferta.getHoraInicio();
                                LocalTime horaFimVinculo = oferta.getHoraTermino();
                                if (horariosConflitam(horaInicioOficina, horaFimOficina, horaInicioVinculo, horaFimVinculo)) {
                                    temConflito = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (temConflito)
                        break;
                }
            }

            if (!temConflito) {
                Map<String, Object> json = new HashMap<>();
                json.put("id", alunoId);
                json.put("nome", pessoa.getNome());
                json.put("mae", aluno.getMae());
                json.put("pai", aluno.getPai());
                json.put("ativo", String.valueOf(pessoa.getAtivo()));
                alunosDisponiveis.add(json);
            }
        }
        return alunosDisponiveis;
    }

}

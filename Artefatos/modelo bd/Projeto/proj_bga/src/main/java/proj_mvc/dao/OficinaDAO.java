package proj_mvc.dao;

import org.springframework.stereotype.Repository;
import proj_mvc.model.Oficina;
import proj_mvc.util.SingletonDB;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class OficinaDAO implements IDAO<Oficina> {

    @Override
    public Oficina gravar(Oficina oficina){
        String sql = """
                        INSERT INTO oficina (
                            ofc_nome, ofc_hora_inicio, ofc_hora_termino, ofc_dt_inicial, ofc_dt_final, prof_id, ofc_ativo)
                            VALUES (#1, #2, #3, #4, #5, #6, #7);
                   """;

        sql = sql.replace("#1", oficina.getNome());
        sql = sql.replace("#2", oficina.getHoraInicio().toString());
        sql = sql.replace("#3", oficina.getHoraTermino().toString());
        sql = sql.replace("#4", oficina.getDataInicio().toString());
        sql = sql.replace("#5", oficina.getDataFim().toString());
        sql = sql.replace("#6", "" + oficina.getProfessor());
        sql = sql.replace("#7", "" + oficina.getAtivo());

        ResultSet resultado = SingletonDB.getConexao().consulta(sql);
        try {
            if(resultado.next()){
                oficina.setId(resultado.getInt("ofc_id"));
                return oficina;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao gravar Oficina: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Oficina alterar(Oficina oficina){
        String sql = """
                        UPDATE oficina
                            SET , ofc_nome = #1, ofc_hora_inicio = #2, ofc_hora_termino = #3, ofc_dt_inicial = #4, ofc_dt_final = #5, prof_id = #6, ofc_ativo = #7
                            WHERE ofc_id = #8;
                    """;

        sql = sql.replace("#1", oficina.getNome());
        sql = sql.replace("#2", oficina.getHoraInicio().toString());
        sql = sql.replace("#3", oficina.getHoraTermino().toString());
        sql = sql.replace("#4", oficina.getDataInicio().toString());
        sql = sql.replace("#5", oficina.getDataFim().toString());
        sql = sql.replace("#6", "" + oficina.getProfessor());
        sql = sql.replace("#7", "" + oficina.getAtivo());
        sql = sql.replace("#8", "" + oficina.getId());

        if(SingletonDB.getConexao().manipula(sql))
            return oficina;
        else{
            System.out.println("Erro ao alterar Oficina: " + SingletonDB.getConexao().getErro());
            return null;
        }
    }


}

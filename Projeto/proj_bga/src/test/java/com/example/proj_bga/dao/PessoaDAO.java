package com.example.proj_bga.dao;

import com.example.proj_bga.model.Aluno;
import com.example.proj_bga.model.Pessoa;
import com.example.proj_bga.util.Conexao;
import com.example.proj_bga.util.SingletonDB;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PessoaDAO implements IDAO<Pessoa>{
    @Override
    public Object gravar(Pessoa entidade, Conexao conexao) {
        return null;
    }

    @Override
    public Object alterar(Pessoa pessoa,Conexao conexao) {

        String sql = """
                    UPDATE public.pessoa
                    SET pes_nome= '#1', pes_cpf='#2', pes_dt_nascimento='#3', pes_rg='#4', pes_ativo= '#5', end_id=#6
                    WHERE pes_id = #7;
                """;
        sql = sql.replace("#1", pessoa.getNome());
        sql = sql.replace("#2", pessoa.getcpf());
        sql = sql.replace("#3",""+ pessoa.getDt_nascimento());
        sql = sql.replace("#4", pessoa.getRg());
        sql = sql.replace("#5", ""+pessoa.getAtivo());
        sql = sql.replace("#6", "" + pessoa.getEnd_id());
        sql = sql.replace("#7", "" + pessoa.getId());



        if(conexao.manipular(sql)){
            return pessoa;
        }
        else{
            System.out.println("Erro ao alterar pessoa:" + conexao.getMensagemErro());
            return null;
        }
    }

    @Override
    public boolean excluir(Pessoa entidade, Conexao conexao) {
        return false;
    }

    @Override
    public List<Pessoa> get(String filtro, Conexao conexao) {
        List<Pessoa> lista = new ArrayList<>();
        String sql = "select * from pessoa ";
        if(!filtro.isEmpty())
            sql = sql + filtro;

        try {
            ResultSet rs = conexao.consultar(sql);
            while (rs.next()) {
                Pessoa pessoa = new Pessoa();
                pessoa.setId(rs.getInt("pes_id"));
                pessoa.setNome(rs.getString("pes_nome"));
                lista.add(pessoa);
            }
            return lista;
        } catch (SQLException e) {
            System.out.println("Erro ao buscar aluno: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Pessoa get(int id,Conexao conexao) {

        Pessoa p = null;
        String sql = """
                SELECT * FROM pessoa
                WHERE pes_id = #1;
            """;

        sql = sql.replace("#1", ""+id);
        ResultSet rs = conexao.consultar(sql);

        try {
            if(rs.next()){
                p = new Pessoa();
                p.setId(rs.getInt("pes_id"));
                p.setNome(rs.getString("pes_nome"));
                p.setcpf(rs.getString("pes_cpf"));
                p.setDt_nascimento(rs.getDate("pes_dt_nascimento").toLocalDate());
                p.setRg(rs.getString("pes_rg"));
                p.setAtivo(rs.getString("pes_ativo").charAt(0));
                p.setEnd_id(rs.getInt("end_id"));
            }
        } catch(Exception e) {
            System.out.println("Erro ao obter Pessoa: " + e.getMessage());
        }
        return p;
    }
}

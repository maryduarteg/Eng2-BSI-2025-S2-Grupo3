package com.example.proj_bga.dao;

import com.example.proj_bga.util.Conexao;

import java.util.List;

public interface IDAO<T>{
    public Object gravar(T entidade, Conexao conexao);
    public Object alterar(T entidade, Conexao conexao);
    public boolean excluir(T entidade, Conexao conexao);
    public List<T> get(String filtro, Conexao conexao);
    public T get(int id, Conexao conexao);
}

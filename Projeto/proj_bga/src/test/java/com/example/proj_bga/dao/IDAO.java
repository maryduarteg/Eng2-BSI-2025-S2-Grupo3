package com.example.proj_bga.dao;

import java.util.List;

public interface IDAO<T>{
    public Object gravar(T entidade);
    public Object alterar(T entidade);
    public boolean excluir(T entidade);
    public List<T> get(String filtro);
}

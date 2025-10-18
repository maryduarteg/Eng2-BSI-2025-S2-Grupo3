package com.example.proj_bga.util;

public class SingletonDB {

    private static Conexao conexao=null;

    private SingletonDB() {
    }

    public static boolean conectar()
    {
        conexao=new Conexao();
        return conexao.conectar("jdbc:postgresql://localhost:5432/","bga-db","postgres","postgres123");
    }
    public static Conexao getConexao() {
        return conexao;
    }
}

package com.example.proj_bga.util;

public class SingletonDB {

    private static Conexao conexao=null;

    private SingletonDB() {
    }

    public static boolean conectar()
    {

        if(conexao == null)
        {
            conexao=new Conexao();
            conexao.conectar("jdbc:postgresql://localhost:5432/","bga-db","postgres","postgres123");
        }
        return conexao.getEstadoConexao();
    }
    public static Conexao getConexao() {
        return conexao;
    }
}

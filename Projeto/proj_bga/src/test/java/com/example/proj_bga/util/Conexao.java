package com.example.proj_bga.util;

import java.sql.*;

public class Conexao {
    static private Connection connect;
    private String erro;

    public Conexao() {
        erro="";
    }

    public Connection getConnect() {
        return connect;
    }

    public boolean conectar(String local, String banco, String usuario, String senha) {
        boolean conectado=false;
        try {
            String url = local+banco;
            connect = DriverManager.getConnection( url, usuario,senha);
            conectado=true;
        }
        catch ( SQLException sqlex ) {
            erro="Impossivel conectar com a base de dados: " + sqlex.getMessage();
            sqlex.printStackTrace();
        }
        catch ( Exception ex ) {
            erro="Outro erro: " + ex.getMessage();
            ex.printStackTrace();
        }
        return conectado;
    }

    public String getMensagemErro() {
        return erro;
    }

    public boolean getEstadoConexao() {
        return (connect!=null);
    }

    public boolean manipular(String sql){
        boolean executou=false;
        try {
            Statement statement = connect.createStatement();
            int result = statement.executeUpdate( sql );
            statement.close();
            if(result>=1)
                executou=true;
        }
        catch ( SQLException sqlex ) {
            erro="Erro: "+sqlex.toString();
        }
        return executou;
    }

    public ResultSet consultar(String sql) {
        ResultSet rs = null;
        try {
            Statement statement = connect.createStatement();
            rs = statement.executeQuery( sql );
        }
        catch ( SQLException sqlex ) {
            erro="Erro: " + sqlex.toString();
        }
        return rs;
    }

    public int getMaxPK(String tabela,String chave) {
        String sql="select max("+chave+") from "+tabela;
        int max=0;
        ResultSet rs = consultar(sql);
        try {
            if(rs.next())
                max=rs.getInt(1);
        }
        catch (SQLException sqlex) {
            erro="Erro: " + sqlex.toString();
            max = -1;
        }
        return max;
    }

    public int consultarValor(String sql) {
        int valor = -1;
        try {
            ResultSet rs = consultar(sql);
            if (rs != null && rs.next()) {
                valor = rs.getInt(1);
            }
        } catch (SQLException e) {
            erro = "Erro ao consultar valor: " + e.getMessage();
        }
        return valor;
    }

}
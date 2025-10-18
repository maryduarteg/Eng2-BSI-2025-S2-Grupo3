package com.example.proj_bga.util;
import java.sql.*;

public class Conexao {
    static private Connection connect;
    private String erro;

    public Conexao()
    {   erro="";
        connect=null;
    }

    public Connection getConnect() {
        return connect;
    }

    public boolean conectar(String local, String banco, String usuario, String senha)
    {   boolean conectado=false;
        try {
            //Class.forName(driver); "org.postgresql.Driver");
            String url = local+banco; //"jdbc:postgresql://localhost/"+banco;
            connect = DriverManager.getConnection( url, usuario,senha);
            conectado=true;
        }
        catch ( SQLException sqlex )
        { erro="Impossivel conectar com a base de dados: " + sqlex.toString(); }
        catch ( Exception ex )
        { erro="Outro erro: " + ex.toString(); }
        return conectado;
    }
    public String getMensagemErro() {
        return erro;
    }

    public boolean getEstadoConexao() {
        return (connect!=null);
    }

    public boolean manipular(String sql) // inserir, alterar,excluir
    {   boolean executou=false;
        try {
            Statement statement = connect.createStatement();
            int result = statement.executeUpdate( sql );
            statement.close();
            if(result>=1)
                executou=true;
        }
        catch ( SQLException sqlex )
        {  erro="Erro: "+sqlex.toString();
        }
        return executou;
    }

    public ResultSet consultar(String sql)
    {   ResultSet rs = null;
        try {
            Statement statement = connect.createStatement();
            //ResultSet.TYPE_SCROLL_INSENSITIVE,
            //ResultSet.CONCUR_READ_ONLY);
            //statement.close();
        }
        catch ( SQLException sqlex ) {
            erro = "Erro: "+sqlex.toString();
        }
        return rs;
    }
}

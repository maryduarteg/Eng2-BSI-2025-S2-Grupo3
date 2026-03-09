package proj_mvc.util;

import org.hibernate.tool.schema.spi.SqlScriptException;

import java.sql.*;

public class Conexao
{

    static private Connection conexao;
    private String erro;
    public Conexao()
    {
        erro = "";
        conexao = null;
    }

    public boolean conectar(String local, String banco, String usuario, String senha)
    {
        boolean conectado = false;
        try{
            String url = local + banco; // resultado esperado => jdbc:postgresql://localhost/"+banco;
            conexao = DriverManager.getConnection(url, usuario, senha);
            conectado = true;
        }
        catch (SqlScriptException sqlex) {
            erro = "Erro ao conectar-se: " + sqlex.getMessage();
        }
        catch (Exception e) {
            erro  = "Erro: " + e.getMessage();
        }
        return conectado;
    }

    public String getErro()
    {
        return erro;
    }

    public boolean manipula(String sql)
    {
        boolean manipulacao = false;
        try{
            Statement statement = conexao.createStatement();
            int resultado = statement.executeUpdate(sql);
            statement.close();
            if  (resultado > 0)
                manipulacao = true;
        }
        catch (SQLException sqlex) {
            erro = "Erro ao manipular dados: " + sqlex.getMessage();
        }
        return manipulacao;
    }

    public ResultSet consulta(String sql)
    {
        ResultSet resultado = null;
        try {
            Statement statement = conexao.createStatement();
            resultado = statement.executeQuery(sql);
        }
        catch (SQLException sqlex)
        {
            erro = "Erro ao consultar dados: " + sqlex.getMessage();
            resultado = null;
        }
        return resultado;
    }
}

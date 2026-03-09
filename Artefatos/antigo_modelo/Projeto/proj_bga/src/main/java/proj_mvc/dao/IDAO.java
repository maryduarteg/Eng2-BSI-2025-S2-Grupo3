package proj_mvc.dao;
import java.util.List;

public interface IDAO<T> {
    public Object gravar(T entidade);
    public Object alterar(T entidade);
}

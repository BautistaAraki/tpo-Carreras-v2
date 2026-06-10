package repositorio;
import Modelo1.Caballo;
import java.util.List;
public interface ICaballoRepositorio {
	void guardar(Caballo caballo);
    void actualizar(Caballo caballo);
    Caballo buscarPorNombre(String nombre);
    List<Caballo> listarTodos();
}

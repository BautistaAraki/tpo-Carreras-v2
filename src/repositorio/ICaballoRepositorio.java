package repositorio;

import java.util.List;
import java.util.Optional;
import Modelo1.Caballo;

public interface ICaballoRepositorio {
    Caballo guardar(Caballo caballo);
    Caballo actualizar(Caballo caballo);
    Optional<Caballo> buscarPorNombre(String nombre);
    List<Caballo> listarTodos();
}

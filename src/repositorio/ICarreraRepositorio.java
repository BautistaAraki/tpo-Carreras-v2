package repositorio;
import Modelo1.Carrera;
import java.util.List;

public interface ICarreraRepositorio {
	void guardar(Carrera carrera);
    List<Carrera> listarTodas();
}

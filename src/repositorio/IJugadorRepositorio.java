package repositorio;

import Modelo1.jugador;
import java.util.List;
public interface IJugadorRepositorio {
	void guardarJugador(jugador jugador);
    jugador buscarPorMail(String mail);
    List<jugador> listarTodos();
}

package repositorio;

import java.util.Optional;
import Modelo1.Jugador;

public interface IJugadorRepositorio {
    Jugador guardar(Jugador jugador);
    Optional<Jugador> buscarPorMail(String mail);
}

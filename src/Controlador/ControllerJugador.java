package Controlador;
import Dto.JugadorDTO;
import Modelo1.jugador;
import Modelo1.Caballo;

public class ControllerJugador {
	private jugador jugador;

    public void crearJugador(JugadorDTO dto) {

        jugador = new jugador(
                dto.getNombre(),
                dto.getMail()
        );
    }

    public void seleccionarCaballo(Caballo caballo) {

        if (jugador != null) {
            jugador.seleccionarCaballo(caballo);
        }
    }

    public int obtenerPuntaje() {

        if (jugador != null) {
            return jugador.getPuntaje();
        }

        return 0;
    }

    public String obtenerNombreJugador() {

        if (jugador != null) {
            return jugador.getnombre();
        }

        return "";
    }

    public Caballo obtenerCaballoSeleccionado() {

        if (jugador != null) {
            return jugador.getcaballoseleccionado();
        }

        return null;
    }

    public jugador getJugador() {
        return jugador;
    }

}

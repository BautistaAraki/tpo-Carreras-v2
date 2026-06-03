package Controlador;
import java.util.List;

import Dto.JugadorDTO;
import Modelo1.Caballo;
import Modelo1.Carrera;
import Modelo1.jugador;

public class controllerCarrera {

    private Carrera carrera;

    public void crearCarrera(
            double distancia,
            List<Caballo> caballos,
            jugador jugador) {

        carrera = new Carrera(
                distancia,
                caballos,
                jugador
        );
    }

    public void iniciarCarrera() {

        if (carrera != null) {
            carrera.iniciarCarrera();
        }
    }

    public void simularTurno() {

        if (carrera != null) {
            carrera.simularTurno();
        }
    }

    public boolean carreraFinalizada() {

        if (carrera != null) {
            return carrera.estaFinalizada();
        }

        return false;
    }

    public Caballo obtenerGanador() {

        if (carrera != null) {
            return carrera.determinarGanador();
        }

        return null;
    }

    public int calcularPuntajeJugador() {

        if (carrera != null) {
            return carrera.calcularPuntajeJugador();
        }

        return 0;
    }

    public List<Caballo> obtenerCaballos() {

        if (carrera != null) {
            return carrera.getCaballosParticipantes();
        }

        return null;
    }

    public Carrera getCarrera() {
        return carrera;
    }
}
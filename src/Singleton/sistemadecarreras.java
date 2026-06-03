package Singleton;

import java.util.ArrayList;
import java.util.List;

import Modelo1.Caballo;
import Modelo1.Carrera;
import Modelo1.jugador;
public class sistemadecarreras {
	private static sistemadecarreras instancia;

    private List<jugador> jugadores;
    private List<Caballo> caballosDisponibles;
    private List<Carrera> carreras;

    private sistemadecarreras() {
        jugadores = new ArrayList<>();
        caballosDisponibles = new ArrayList<>();
        carreras = new ArrayList<>();
    }

    public static sistemadecarreras getInstancia() {

        if (instancia == null) {
            instancia = new sistemadecarreras();
        }

        return instancia;
    }

    public jugador registrarJugador(String nombre, String mail) {

        jugador jugador = new jugador(nombre, mail);

        jugadores.add(jugador);

        return jugador;
    }

    public jugador buscarJugadorPorMail(String mail) {

        for (jugador jugador : jugadores) {

            if (jugador.getmail().equals(mail)) {
                return jugador;
            }
        }

        return null;
    }

    public List<Caballo> obtenerCaballosDisponibles() {
        return caballosDisponibles;
    }
   }


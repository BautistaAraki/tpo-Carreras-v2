package Controlador;

import Dto.JugadorDTO;
import Modelo1.Caballo;
import Modelo1.Jugador;
import database.JpaUtil;
import repositorio.*;

public final class ControllerJugador {
    private static final ControllerJugador INSTANCIA = new ControllerJugador();
    private final IJugadorRepositorio jugadorRepositorio = new JugadorRepositorio();
    private final ICaballoRepositorio caballoRepositorio = new CaballoRepositorio();
    private Jugador jugadorActual;

    private ControllerJugador() {}
    public static ControllerJugador getInstancia() { return INSTANCIA; }

    public JugadorDTO ingresarORegistrar(String nombre, String mail) {
        String mailValido = validarMail(mail);
        var existente = jugadorRepositorio.buscarPorMail(mailValido);
        if (existente.isPresent()) {
            jugadorActual = existente.get();
            return aDTO(jugadorActual);
        }
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El usuario no existe. Ingresá un nombre para crear la cuenta.");
        jugadorActual = jugadorRepositorio.guardar(new Jugador(nombre.trim(), mailValido));
        return aDTO(jugadorActual);
    }

    public JugadorDTO iniciarSesion(String mail) {
        jugadorActual = jugadorRepositorio.buscarPorMail(validarMail(mail))
                .orElseThrow(() -> new IllegalArgumentException("No existe un usuario con ese mail."));
        return aDTO(jugadorActual);
    }
    public JugadorDTO registrar(String nombre, String mail) {
        String mailValido = validarMail(mail);
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("Ingresá un nombre.");
        if (jugadorRepositorio.buscarPorMail(mailValido).isPresent())
            throw new IllegalArgumentException("El usuario ya existe. Usá Ingresar.");
        jugadorActual = jugadorRepositorio.guardar(new Jugador(nombre.trim(), mailValido));
        return aDTO(jugadorActual);
    }
    public JugadorDTO seleccionarCaballo(String nombre) {
        exigirSesion();
        Caballo caballo = caballoRepositorio.buscarPorNombre(nombre)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el caballo."));
        jugadorActual.seleccionarCaballo(caballo);
        return aDTO(jugadorActual);
    }
    public JugadorDTO obtenerJugadorActual() { exigirSesion(); return aDTO(jugadorActual); }
    public boolean haySesionActiva() { return jugadorActual != null; }
    public void persistirSesion() { if (jugadorActual != null) jugadorActual = jugadorRepositorio.guardar(jugadorActual); }
    public void cerrarSesion() { persistirSesion(); jugadorActual = null; }
    public void cerrarAplicacion() { persistirSesion(); JpaUtil.cerrar(); }
    Jugador obtenerEntidadActual() { exigirSesion(); return jugadorActual; }
    void sumarPuntos(int puntos) { exigirSesion(); jugadorActual.sumarPuntos(puntos); persistirSesion(); }

    private void exigirSesion() {
        if (jugadorActual == null) throw new IllegalStateException("Primero debés iniciar sesión.");
    }
    private String validarMail(String mail) {
        if (mail == null || !mail.trim().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))
            throw new IllegalArgumentException("Ingresá un mail válido.");
        return mail.trim().toLowerCase();
    }
    private JugadorDTO aDTO(Jugador jugador) {
        String caballo = jugador.getCaballoSeleccionado() == null ? null : jugador.getCaballoSeleccionado().getNombre();
        return new JugadorDTO(jugador.getNombre(), jugador.getMail(), jugador.getPuntaje(), caballo);
    }
}

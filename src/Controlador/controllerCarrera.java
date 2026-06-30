package Controlador;

import java.util.ArrayList;
import java.util.List;
import Dto.*;
import Modelo1.*;
import repositorio.*;

public final class ControllerCarrera {
    private static final ControllerCarrera INSTANCIA = new ControllerCarrera();
    private final ICaballoRepositorio caballoRepositorio = new CaballoRepositorio();
    private final ICarreraRepositorio carreraRepositorio = new CarreraRepositorio();
    private Carrera carreraActual;
    private boolean resultadoPersistido;

    private ControllerCarrera() {}
    public static ControllerCarrera getInstancia() { return INSTANCIA; }

    public List<CaballoDTO> listarCaballos() {
        inicializarCaballosSiHaceFalta();
        String seleccionado = caballoSeleccionado();
        return caballoRepositorio.listarTodos().stream()
                .map(c -> aDTO(c, c.getNombre().equals(seleccionado))).toList();
    }
    private void inicializarCaballosSiHaceFalta() {
        if (!caballoRepositorio.listarTodos().isEmpty()) return;
        caballoRepositorio.guardar(new Caballo("Relámpago", 9.5, 70, 70, 0, "Veloz"));
        caballoRepositorio.guardar(new Caballo("Trueno", 8.2, 90, 90, 0, "Resistente"));
        caballoRepositorio.guardar(new Caballo("Centella", 8.8, 80, 80, 0, "Equilibrado"));
    }
    public CaballoDTO registrarCaballo(CaballoDTO dto) {
        if (dto.nombre() == null || dto.nombre().isBlank()) throw new IllegalArgumentException("Ingresá un nombre.");
        if (dto.velocidadBase() <= 0 || dto.resistencia() <= 0)
            throw new IllegalArgumentException("Velocidad y resistencia deben ser mayores que cero.");
        if (caballoRepositorio.buscarPorNombre(dto.nombre().trim()).isPresent())
            throw new IllegalArgumentException("Ya existe un caballo con ese nombre.");
        Caballo caballo = new Caballo(dto.nombre().trim(), dto.velocidadBase(), dto.resistencia(),
                dto.resistencia(), 0, dto.perfil());
        return aDTO(caballoRepositorio.guardar(caballo), false);
    }
    public CarreraDTO crearCarrera(double distancia, List<String> nombres) {
        Jugador jugador = ControllerJugador.getInstancia().obtenerEntidadActual();
        if (jugador.getCaballoSeleccionado() == null) throw new IllegalStateException("Seleccioná un caballo.");
        List<Caballo> participantes = new ArrayList<>();
        for (String nombre : nombres) {
            Caballo original = caballoRepositorio.buscarPorNombre(nombre)
                    .orElseThrow(() -> new IllegalArgumentException("No existe " + nombre));
            Caballo copia = original.clonarParaCarrera();
            participantes.add(copia);
            if (nombre.equals(jugador.getCaballoSeleccionado().getNombre())) jugador.seleccionarCaballo(copia);
        }
        if (participantes.size() < 2) throw new IllegalArgumentException("Se necesitan al menos dos caballos.");
        carreraActual = new Carrera(distancia, participantes, jugador);
        carreraActual.iniciarCarrera();
        resultadoPersistido = false;
        return estado();
    }
    public CarreraDTO simularTurno() {
        exigirCarrera();
        carreraActual.simularTurno();
        if (carreraActual.estaFinalizada() && !resultadoPersistido) persistirResultado();
        return estado();
    }
    public CarreraDTO obtenerEstado() { exigirCarrera(); return estado(); }
    private void persistirResultado() {
        int puntos = carreraActual.calcularPuntajeJugador();
        carreraActual.registrarResultado(puntos);
        ControllerJugador.getInstancia().sumarPuntos(puntos);
        carreraRepositorio.guardar(carreraActual);
        resultadoPersistido = true;
    }
    private CarreraDTO estado() {
        String seleccionado = caballoSeleccionado();
        String ganador = carreraActual.estaFinalizada() && carreraActual.determinarGanador() != null
                ? carreraActual.determinarGanador().getNombre() : null;
        int puntos = resultadoPersistido ? carreraActual.getPuntosOtorgados() : 0;
        int total = ControllerJugador.getInstancia().obtenerJugadorActual().puntaje();
        List<CaballoDTO> caballos = carreraActual.getCaballosParticipantes().stream()
                .map(c -> aDTO(c, c.getNombre().equals(seleccionado))).toList();
        return new CarreraDTO(carreraActual.getDistanciaTotal(), carreraActual.estaFinalizada(), caballos, ganador, puntos, total);
    }
    private String caballoSeleccionado() {
        try { return ControllerJugador.getInstancia().obtenerJugadorActual().caballoSeleccionado(); }
        catch (IllegalStateException ex) { return null; }
    }
    private CaballoDTO aDTO(Caballo c, boolean seleccionado) {
        return new CaballoDTO(c.getNombre(), c.getVelocidadBase(), c.getResistencia(),
                c.getEnergiaActual(), c.getDistanciaRecorrida(), c.getPerfil(), seleccionado);
    }
    private void exigirCarrera() {
        if (carreraActual == null) throw new IllegalStateException("No hay una carrera activa.");
    }
}


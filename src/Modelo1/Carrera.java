package Modelo1;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "carrera")
public class Carrera implements ICarrera {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "distancia_total", nullable = false)
    private double distanciaTotal;
    @Column(nullable = false)
    private boolean finalizada;
    @Column(name = "fecha_hora")
    private LocalDateTime fechaHoraInicial;
    @Column(name = "ganador", length = 80)
    private String nombreGanador;
    @Column(name = "jugador_mail", length = 120)
    private String jugadorMail;
    @Column(name = "puntos_otorgados")
    private int puntosOtorgados;
    @Transient
    private List<Caballo> caballosParticipantes = new ArrayList<>();
    @Transient
    private Jugador jugadorParticipante;

    protected Carrera() {}

    public Carrera(double distanciaTotal, List<Caballo> caballos, Jugador jugador) {
        this.distanciaTotal = distanciaTotal;
        this.caballosParticipantes = caballos;
        this.jugadorParticipante = jugador;
        this.jugadorMail = jugador.getMail();
        this.fechaHoraInicial = LocalDateTime.now();
    }

    public void iniciarCarrera() { caballosParticipantes.forEach(Caballo::reiniciarAtributos); finalizada = false; }
    public void simularTurno() {
        caballosParticipantes.forEach(Caballo::avanzar);
        finalizada = caballosParticipantes.stream().anyMatch(c -> c.getDistanciaRecorrida() >= distanciaTotal);
    }
    public Caballo determinarGanador() {
        return caballosParticipantes.stream().max(Comparator.comparingDouble(Caballo::getDistanciaRecorrida)).orElse(null);
    }
    public int calcularPuntajeJugador() {
        List<Caballo> ordenados = new ArrayList<>(caballosParticipantes);
        ordenados.sort(Comparator.comparingDouble(Caballo::getDistanciaRecorrida).reversed());
        Caballo elegido = jugadorParticipante.getCaballoSeleccionado();
        if (elegido == null || ordenados.isEmpty()) return 0;
        double primero = ordenados.get(0).getDistanciaRecorrida();
        long empatados = ordenados.stream().filter(c -> Double.compare(c.getDistanciaRecorrida(), primero) == 0).count();
        double distanciaJugador = elegido.getDistanciaRecorrida();
        if (Double.compare(distanciaJugador, primero) == 0) return empatados > 1 ? 75 : 100;
        if (ordenados.size() > 1 && Double.compare(distanciaJugador, ordenados.get(1).getDistanciaRecorrida()) == 0) return 55;
        return 10;
    }
    public void registrarResultado(int puntos) {
        Caballo ganador = determinarGanador();
        nombreGanador = ganador == null ? null : ganador.getNombre();
        puntosOtorgados = puntos;
        finalizada = true;
    }
    public Long getId() { return id; }
    public double getDistanciaTotal() { return distanciaTotal; }
    public boolean estaFinalizada() { return finalizada; }
    public List<Caballo> getCaballosParticipantes() { return caballosParticipantes; }
    public String getNombreGanador() { return nombreGanador; }
    public int getPuntosOtorgados() { return puntosOtorgados; }
}

	

}

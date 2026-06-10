package Modelo1;
import java.util.List;
public interface ICarrera {
	void iniciarCarrera();
    void simularTurno();
    boolean estaFinalizada();
    Caballo determinarGanador();
    int calcularPuntajeJugador();
    List<Caballo> getCaballosParticipantes();
    Double getDistanciaTotal();
}

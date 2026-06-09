package Modelo1;
import java.util.List;
import java.util.Date;

public class Carrera {
	private Double distanciaTotal;
	private Boolean finalizada;
	private List<Caballo> caballoParticipantes;
	private jugador jugadorParticipante;
	private Date fechaHoraInicial;
	private Caballo ganador;
	public Carrera(
	        Double distanciaTotal,
	        List<Caballo> caballoParticipantes,
	        jugador jugadorParticipante) {

	    this.distanciaTotal = distanciaTotal;
	    this.caballoParticipantes = caballoParticipantes;
	    this.jugadorParticipante = jugadorParticipante;
	    this.finalizada = false;
	}
	public void iniciarCarrera() {

        for (Caballo caballo : caballoParticipantes) {
            caballo.ReinciarAtributos();
        }

        finalizada = false;
    }

    public void simularTurno() {

        for (Caballo caballo : caballoParticipantes) {
            caballo.avanzar();
        }

        verificarFinalizacion();
    }

    public boolean verificarFinalizacion() {

        for (Caballo caballo : caballoParticipantes) {

            if (caballo.getDistanciaRecorrida() >= distanciaTotal) {
                finalizada = true;
                return true;
            }
        }

        return false;
    }

    public Caballo determinarGanador() {

        Caballo ganador = caballoParticipantes.get(0);

        for (Caballo caballo : caballoParticipantes) {

            if (caballo.getDistanciaRecorrida() >
                ganador.getDistanciaRecorrida()) {

                ganador = caballo;
            }
        }

        return ganador;
    }

    public int calcularPuntajeJugador() {

        double maxDistancia = 0;
        for (Caballo c : caballoParticipantes) {
            if (c.getDistanciaRecorrida() > maxDistancia) {
                maxDistancia = c.getDistanciaRecorrida();
            }
        }

       
        int empatados = 0;
        for (Caballo c : caballoParticipantes) {
            if (c.getDistanciaRecorrida() == maxDistancia) {
                empatados++;
            }
        }

        Caballo caballoJugador = jugadorParticipante.getcaballoseleccionado();

        
        if (caballoJugador.getDistanciaRecorrida() == maxDistancia) {
            if (empatados > 1) {
                return 75; 
            }
            return 100;
        }

        return 10; 
    }

    public List<Caballo> getCaballosParticipantes() {
        return caballoParticipantes;
    }

    public boolean estaFinalizada() {
        return finalizada;
    }
    public Double getDistanciaTotal() {
        return distanciaTotal;
    }

    public jugador getJugadorParticipante() {
        return jugadorParticipante;
    }
		

}

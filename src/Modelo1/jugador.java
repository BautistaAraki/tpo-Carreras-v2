package Modelo1;

public class jugador {
	private String nombre;
	private String mail;
	private int puntajeActual;
	private Caballo seleccionarCaballo;
	private Boolean esIA;
	public void seleccionarCaballo(Caballo caballo){
		seleccionarCaballo=caballo;	
	}
	public jugador(String nombre, String mail) {
	        this.nombre = nombre;
	        this.mail = mail;
	        this.puntajeActual = 0;
	    }
	public void sumarPuntos(int puntos) {
	    puntajeActual = puntajeActual + puntos;
	}
	public Caballo getcaballoseleccionado() {
		return seleccionarCaballo;
	}
	public String getnombre () {
		return nombre;
	}
	public String getmail () {
		return mail;
	}
	public int getPuntaje() {
		return puntajeActual;
	}

}

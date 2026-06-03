package Dto;

public class CaballoDTO {
	 private String nombre;
	    private double velocidadBase;
	    private double resistencia;
	    private double energiaActual;
	    private double distanciaRecorrida;
	    private String perfil;

	    public CaballoDTO() {
	    }

	    public CaballoDTO(String nombre,
	                      double velocidadBase,
	                      double resistencia,
	                      double energiaActual,
	                      double distanciaRecorrida,
	                      String perfil) {

	        this.nombre = nombre;
	        this.velocidadBase = velocidadBase;
	        this.resistencia = resistencia;
	        this.energiaActual = energiaActual;
	        this.distanciaRecorrida = distanciaRecorrida;
	        this.perfil = perfil;
	    }

	    public String getNombre() {
	        return nombre;
	    }

	    public double getVelocidadBase() {
	        return velocidadBase;
	    }

	    public double getResistencia() {
	        return resistencia;
	    }

	    public double getEnergiaActual() {
	        return energiaActual;
	    }

	    public double getDistanciaRecorrida() {
	        return distanciaRecorrida;
	    }

	    public String getPerfil() {
	        return perfil;
	    }

}

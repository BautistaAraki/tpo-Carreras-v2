package Modelo1;

public class Caballo {
	private String nombre;
	private Double velocidadBase ;
	private Double resistencia;
	private Double energiaActual;
	private Double distanciaRecorrida;
	private String perfil;
	private IPerfilCaballo estrategiaPerfil;

	public Caballo(String nombre, Double velocidadBase, Double resistencia,
	               Double energiaActual, Double distanciaRecorrida, String perfil) {
	    this.nombre = nombre;
	    this.velocidadBase = velocidadBase;
	    this.resistencia = resistencia;
	    this.energiaActual = energiaActual;
	    this.distanciaRecorrida = distanciaRecorrida;
	    this.perfil = perfil;
	
	  
	    switch (perfil) {
	        case "Veloz":       this.estrategiaPerfil = new PerfilVeloz();       break;
	        case "Resistente":  this.estrategiaPerfil = new PerfilResistente();  break;
	        default:            this.estrategiaPerfil = new PerfilEquilibrado(); break;
	    }
}
	public Caballo clonar() {
	    return new Caballo(
	        this.nombre,
	        this.velocidadBase,
	        this.resistencia,
	        this.resistencia,  
	        0.0,              
	        this.perfil
	    );
	}
	public void avanzar() {
	    if (energiaActual > 0) {
	        double avance = estrategiaPerfil.calcularAvance(velocidadBase, energiaActual, resistencia);
	        distanciaRecorrida = distanciaRecorrida + avance;
	        disminuirEnergia();
	    }
	}
	public void disminuirEnergia() {
		energiaActual = energiaActual - 0.5;

	    if (energiaActual < 0) {
	        energiaActual = 0.0;
	    }
	    resistencia = resistencia - 0.1;
	    if (resistencia < 1.0) {
	        resistencia = 1.0; 
	    }
		
	    
	}
	public void aplicarDesgasteEntreCarreras() {
	    
	    resistencia = resistencia - 5.0;
	    if (resistencia < 10.0) {
	        resistencia = 10.0; 
	    }

	    velocidadBase = velocidadBase - 0.5;
	    if (velocidadBase < 2.0) {
	        velocidadBase = 2.0;
	    }
	}
	public void ReinciarAtributos() {
		energiaActual=resistencia;
		distanciaRecorrida=(double)0;
		
	}
	public String getNombre() {
		return nombre;
	}
	public Double getDistanciaRecorrida() {
		return distanciaRecorrida;
	}
	public Double getEnergiaActual() {
		return energiaActual;
	}
	public String getperfil() {
		return perfil;
	}
	public Double getVelocidadBase() {
	    return velocidadBase;
	}

	public Double getResistencia() {
	    return resistencia;
	}
		
		
}
		
}


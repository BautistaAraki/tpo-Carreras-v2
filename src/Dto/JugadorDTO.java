package Dto;

public class JugadorDTO {
	private String nombre;
	private String mail;
	private int puntaje;
	 public JugadorDTO(String nombre, String mail, int puntaje) {
	        this.nombre = nombre;
	        this.mail = mail;
	        this.puntaje = puntaje;
	    }

	    public String getNombre() {
	        return nombre;
	    }

	    public void setNombre(String nombre) {
	        this.nombre = nombre;
	    }

	    public String getMail() {
	        return mail;
	    }

	    public void setMail(String mail) {
	        this.mail = mail;
	    }

	    public int getPuntaje() {
	        return puntaje;
	    }

	    public void setPuntaje(int puntaje) {
	        this.puntaje = puntaje;
	    }

}

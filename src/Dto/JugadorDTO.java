package Dto;

public record JugadorDTO(String nombre, String mail, int puntaje, String caballoSeleccionado) {
    public JugadorDTO(String nombre, String mail) { this(nombre, mail, 0, null); }
}


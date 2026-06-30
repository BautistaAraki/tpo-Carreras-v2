package Dto;

public record CaballoDTO(String nombre, double velocidadBase, double resistencia,
        double energiaActual, double distanciaRecorrida, String perfil, boolean seleccionado) {
    public CaballoDTO(String nombre, double velocidadBase, double resistencia, String perfil) {
        this(nombre, velocidadBase, resistencia, resistencia, 0, perfil, false);
    }
}


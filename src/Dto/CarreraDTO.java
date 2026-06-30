package Dto;

import java.util.List;

public record CarreraDTO(double distanciaTotal, boolean finalizada, List<CaballoDTO> caballos,
        String ganador, int puntosObtenidos, int puntajeTotal) {}

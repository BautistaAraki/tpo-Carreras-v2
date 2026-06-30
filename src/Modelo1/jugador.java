package Modelo1;

import jakarta.persistence.*;

@Entity
@Table(name = "jugador")
public class Jugador {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 80)
    private String nombre;
    @Column(nullable = false, unique = true, length = 120)
    private String mail;
    @Column(name = "puntaje", nullable = false)
    private int puntajeActual;
    @Transient
    private Caballo caballoSeleccionado;

    protected Jugador() {}
    public Jugador(String nombre, String mail) { this.nombre = nombre; this.mail = mail; }
    public void seleccionarCaballo(Caballo caballo) { caballoSeleccionado = caballo; }
    public void sumarPuntos(int puntos) { puntajeActual += puntos; }
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getMail() { return mail; }
    public int getPuntaje() { return puntajeActual; }
    public Caballo getCaballoSeleccionado() { return caballoSeleccionado; }
}


}

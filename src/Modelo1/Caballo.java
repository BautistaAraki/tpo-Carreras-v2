package Modelo1;

import jakarta.persistence.*;

@Entity
@Table(name = "caballo")
public class Caballo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 80)
    private String nombre;
    @Column(name = "velocidad_base", nullable = false)
    private double velocidadBase;
    @Column(nullable = false)
    private double resistencia;
    @Column(name = "energia_actual", nullable = false)
    private double energiaActual;
    @Column(name = "distancia_recorrida", nullable = false)
    private double distanciaRecorrida;
    @Column(nullable = false, length = 20)
    private String perfil;
    @Transient
    private IPerfilCaballo estrategiaPerfil;

    protected Caballo() {}

    public Caballo(String nombre, double velocidadBase, double resistencia,
                   double energiaActual, double distanciaRecorrida, String perfil) {
        this.nombre = nombre;
        this.velocidadBase = velocidadBase;
        this.resistencia = resistencia;
        this.energiaActual = energiaActual;
        this.distanciaRecorrida = distanciaRecorrida;
        this.perfil = perfil;
        configurarPerfil();
    }

    private void configurarPerfil() {
        estrategiaPerfil = switch (perfil) {
            case "Veloz" -> new PerfilVeloz();
            case "Resistente" -> new PerfilResistente();
            default -> new PerfilEquilibrado();
        };
    }

    public Caballo clonarParaCarrera() {
        return new Caballo(nombre, velocidadBase, resistencia, resistencia, 0.0, perfil);
    }

    public void avanzar() {
        if (energiaActual <= 0) return;
        if (estrategiaPerfil == null) configurarPerfil();
        distanciaRecorrida += estrategiaPerfil.calcularAvance(velocidadBase, energiaActual, resistencia);
        energiaActual = Math.max(0, energiaActual - 0.5);
    }

    public void aplicarDesgasteEntreCarreras() {
        resistencia = Math.max(10, resistencia - 5);
        velocidadBase = Math.max(2, velocidadBase - 0.5);
        reiniciarAtributos();
    }

    public void reiniciarAtributos() {
        energiaActual = resistencia;
        distanciaRecorrida = 0;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public double getVelocidadBase() { return velocidadBase; }
    public double getResistencia() { return resistencia; }
    public double getEnergiaActual() { return energiaActual; }
    public double getDistanciaRecorrida() { return distanciaRecorrida; }
    public String getPerfil() { return perfil; }
}


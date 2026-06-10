package Modelo1;

public class PerfilEquilibrado implements IPerfilCaballo {

	@Override
	public double calcularAvance(double velocidadBase, double energiaActual, double resistencia) {
		double random = 0.7 + (Math.random() * 0.6);
        return velocidadBase * (energiaActual / resistencia) * random;
	}

}

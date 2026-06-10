package Modelo1;

public class PerfilResistente implements IPerfilCaballo {

	@Override
	public double calcularAvance(double velocidadBase, double energiaActual, double resistencia) {
		double random = 0.7 + (Math.random() * 0.4);
        return velocidadBase * (energiaActual / resistencia) * random * 0.9;
	}

}

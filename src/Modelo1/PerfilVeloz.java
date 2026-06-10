package Modelo1;

public class PerfilVeloz implements IPerfilCaballo {

	@Override
	public double calcularAvance(double velocidadBase, double energiaActual, double resistencia) {
		{
	        double random = 0.8 + (Math.random() * 0.6);
	        return velocidadBase * (energiaActual / resistencia) * random * 1.3;
	    }

	}

}

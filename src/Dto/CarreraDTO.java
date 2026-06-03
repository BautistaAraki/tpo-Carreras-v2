package Dto;

public class CarreraDTO {
	private double distanciaTotal;
    private boolean finalizada;

    public CarreraDTO() {
    }

    public CarreraDTO(double distanciaTotal, boolean finalizada) {
        this.distanciaTotal = distanciaTotal;
        this.finalizada = finalizada;
    }

    public double getDistanciaTotal() {
        return distanciaTotal;
    }

    public boolean isFinalizada() {
        return finalizada;
    }


}

package repositorio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Modelo1.Carrera;
import Modelo1.Caballo;
import Modelo1.jugador;
import database.conexionDB;
public class carrerarepositorio implements ICarreraRepositorio{
	public void guardar(Carrera carrera) {

        try {

            Connection con =
                    conexionDB.obtenerConexion();

            String sql =
                    "INSERT INTO carrera(distancia_total, finalizada) VALUES (?, ?)";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setDouble(1, carrera.getDistanciaTotal());
            ps.setBoolean(2, carrera.estaFinalizada());

            ps.executeUpdate();

            System.out.println("Carrera guardada correctamente");

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public List<Carrera> listarTodas() {

        List<Carrera> carreras =
                new ArrayList<>();

        try {

            Connection con =
                    conexionDB.obtenerConexion();

            String sql =
                    "SELECT * FROM carrera";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                Carrera carrera =
                        new Carrera(
                                rs.getDouble("distancia_total"),
                                new ArrayList<Caballo>(),
                                null
                        );

                carreras.add(carrera);
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return carreras;
    }
	

}


}

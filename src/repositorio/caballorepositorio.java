package repositorio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Modelo1.Caballo;
import database.conexionDB;
public class caballorepositorio {

	    public void guardar(Caballo caballo) {

	        try {

	            Connection con = conexionDB.obtenerConexion();

	            String sql =
	            "INSERT INTO caballo(nombre, velocidad_base, resistencia, energia_actual, distancia_recorrida, perfil) VALUES (?, ?, ?, ?, ?, ?)";

	            PreparedStatement ps =
	                    con.prepareStatement(sql);

	            ps.setString(1, caballo.getNombre());
	            ps.setDouble(2, caballo.getVelocidadBase());
	            ps.setDouble(3, caballo.getResistencia());
	            ps.setDouble(4, caballo.getEnergiaActual());
	            ps.setDouble(5, caballo.getDistanciaRecorrida());
	            ps.setString(6, caballo.getperfil());

	            ps.executeUpdate();

	        } catch (Exception e) {

	            e.printStackTrace();

	        }
	    }

	    public Caballo buscarPorNombre(String nombre) {

	        Caballo caballo = null;

	        try {

	            Connection con = conexionDB.obtenerConexion();

	            String sql =
	                    "SELECT * FROM caballo WHERE nombre = ?";

	            PreparedStatement ps =
	                    con.prepareStatement(sql);

	            ps.setString(1, nombre);

	            ResultSet rs = ps.executeQuery();

	            if (rs.next()) {

	                caballo = new Caballo(
	                        rs.getString("nombre"),
	                        rs.getDouble("velocidad_base"),
	                        rs.getDouble("resistencia"),
	                        rs.getDouble("energia_actual"),
	                        rs.getDouble("distancia_recorrida"),
	                        rs.getString("perfil")
	                );
	            }

	        } catch (Exception e) {

	            e.printStackTrace();

	        }

	        return caballo;
	    }

	    public List<Caballo> listarTodos() {

	        List<Caballo> caballos =
	                new ArrayList<>();

	        try {

	            Connection con = conexionDB.obtenerConexion();

	            String sql = "SELECT * FROM caballo";

	            PreparedStatement ps =
	                    con.prepareStatement(sql);

	            ResultSet rs =
	                    ps.executeQuery();

	            while (rs.next()) {

	                Caballo caballo = new Caballo(
	                        rs.getString("nombre"),
	                        rs.getDouble("velocidad_base"),
	                        rs.getDouble("resistencia"),
	                        rs.getDouble("energia_actual"),
	                        rs.getDouble("distancia_recorrida"),
	                        rs.getString("perfil")
	                );

	                caballos.add(caballo);
	            }

	        } catch (Exception e) {

	            e.printStackTrace();

	        }

	        return caballos;
	    }
	    public void actualizar(Caballo caballo) {
	    	try {
	            Connection con = conexionDB.obtenerConexion();

	            String sql = "UPDATE caballo SET " +
	                         "energia_actual = ?, " +
	                         "distancia_recorrida = ?, " +
	                         "resistencia = ?, " +
	                         "velocidad_base = ? " +  
	                         "WHERE nombre = ?";

	            PreparedStatement ps = con.prepareStatement(sql);
	            ps.setDouble(1, caballo.getEnergiaActual());
	            ps.setDouble(2, caballo.getDistanciaRecorrida());
	            ps.setDouble(3, caballo.getResistencia());
	            ps.setDouble(4, caballo.getVelocidadBase());  
	            ps.setString(5, caballo.getNombre());

	            ps.executeUpdate();

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    	
}

	    

package repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Modelo1.jugador;
import database.conexionDB;

public class jugadorrepositorio  implements IJugadorRepositorio{

    public void guardarJugador(jugador jugador) {

        try {

            Connection con =
                    conexionDB.obtenerConexion();

            String sql =
                    "INSERT INTO jugador(nombre, mail, puntaje) VALUES (?, ?, ?)";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, jugador.getnombre());
            ps.setString(2, jugador.getmail());
            ps.setInt(3, jugador.getPuntaje());

            ps.executeUpdate();

            System.out.println("Jugador guardado correctamente");

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public jugador buscarPorMail(String mail) {

        jugador jugadorEncontrado = null;

        try {

            Connection con =
                    conexionDB.obtenerConexion();

            String sql =
                    "SELECT * FROM jugador WHERE mail = ?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, mail);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                jugadorEncontrado =
                        new jugador(
                                rs.getString("nombre"),
                                rs.getString("mail")
                        );

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return jugadorEncontrado;
    }

    public List<jugador> listarTodos() {

        List<jugador> jugadores =
                new ArrayList<>();

        try {

            Connection con =
                    conexionDB.obtenerConexion();

            String sql =
                    "SELECT * FROM jugador";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                jugador jugador =
                        new jugador(
                                rs.getString("nombre"),
                                rs.getString("mail")
                        );

                jugadores.add(jugador);
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return jugadores;
    }
}

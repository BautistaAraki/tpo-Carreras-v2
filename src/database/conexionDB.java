package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conexionDB {

    private static final String URL =
            "jdbc:mysql://localhost:3306/carreracaballo";

    private static final String USER = "root";

    private static final String PASSWORD = "123456";

    public static Connection obtenerConexion() throws SQLException {

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }
}
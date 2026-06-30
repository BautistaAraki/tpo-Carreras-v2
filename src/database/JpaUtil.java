package database;

import java.util.HashMap;
import java.util.Map;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public final class JpaUtil {
    private static final EntityManagerFactory EMF = crearFactory();

    private JpaUtil() {}

    private static EntityManagerFactory crearFactory() {
        Map<String, String> propiedades = new HashMap<>();
        propiedades.put("jakarta.persistence.jdbc.url", valor("DB_URL", "jdbc:mysql://localhost:3306/carreracaballo?createDatabaseIfNotExist=true"));
        propiedades.put("jakarta.persistence.jdbc.user", valor("DB_USER", "root"));
        propiedades.put("jakarta.persistence.jdbc.password", valor("DB_PASSWORD", "123456"));
        propiedades.put("jakarta.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
        return Persistence.createEntityManagerFactory("carreraPU", propiedades);
    }

    private static String valor(String variable, String predeterminado) {
        String valor = System.getenv(variable);
        return valor == null || valor.isBlank() ? predeterminado : valor;
    }

    public static EntityManager crearEntityManager() { return EMF.createEntityManager(); }
    public static void cerrar() { if (EMF.isOpen()) EMF.close(); }
}

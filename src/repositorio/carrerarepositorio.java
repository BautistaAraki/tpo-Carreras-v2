package repositorio;

import Modelo1.Carrera;
import database.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class CarreraRepositorio implements ICarreraRepositorio {
    public Carrera guardar(Carrera carrera) {
        try (EntityManager em = JpaUtil.crearEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try { tx.begin(); em.persist(carrera); tx.commit(); return carrera; }
            catch (RuntimeException ex) { if (tx.isActive()) tx.rollback(); throw ex; }
        }
    }
}


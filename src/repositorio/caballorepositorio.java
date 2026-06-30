package repositorio;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import Modelo1.Caballo;
import database.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class CaballoRepositorio implements ICaballoRepositorio {
    public Caballo guardar(Caballo caballo) { return transaccion(em -> { em.persist(caballo); return caballo; }); }
    public Caballo actualizar(Caballo caballo) { return transaccion(em -> em.merge(caballo)); }
    public Optional<Caballo> buscarPorNombre(String nombre) {
        try (EntityManager em = JpaUtil.crearEntityManager()) {
            return em.createQuery("select c from Caballo c where c.nombre = :nombre", Caballo.class)
                    .setParameter("nombre", nombre).getResultStream().findFirst();
        }
    }
    public List<Caballo> listarTodos() {
        try (EntityManager em = JpaUtil.crearEntityManager()) {
            return em.createQuery("select c from Caballo c order by c.nombre", Caballo.class).getResultList();
        }
    }
    private Caballo transaccion(Function<EntityManager, Caballo> accion) {
        try (EntityManager em = JpaUtil.crearEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try { tx.begin(); Caballo resultado = accion.apply(em); tx.commit(); return resultado; }
            catch (RuntimeException ex) { if (tx.isActive()) tx.rollback(); throw ex; }
        }
    }
}


	    


	    

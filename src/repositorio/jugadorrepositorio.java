package repositorio;

import java.util.Optional;
import Modelo1.Jugador;
import database.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class JugadorRepositorio implements IJugadorRepositorio {
    public Jugador guardar(Jugador jugador) {
        try (EntityManager em = JpaUtil.crearEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                Jugador resultado;
                if (jugador.getId() == null) { em.persist(jugador); resultado = jugador; }
                else resultado = em.merge(jugador);
                tx.commit();
                return resultado;
            } catch (RuntimeException ex) { if (tx.isActive()) tx.rollback(); throw ex; }
        }
    }
    public Optional<Jugador> buscarPorMail(String mail) {
        try (EntityManager em = JpaUtil.crearEntityManager()) {
            return em.createQuery("select j from Jugador j where lower(j.mail) = lower(:mail)", Jugador.class)
                    .setParameter("mail", mail.trim()).getResultStream().findFirst();
        }
    }
}


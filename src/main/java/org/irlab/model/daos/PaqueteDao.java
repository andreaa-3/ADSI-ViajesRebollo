package org.irlab.model.daos;

import org.irlab.model.entities.Paquete;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

/**
 * DAO class for managing Paquete entities.
 */
public class PaqueteDao {

    /**
     * Saves a new Paquete entity to the database.
     *
     * @param em      the EntityManager to use for the operation
     * @param paquete the Paquete entity to save
     * @throws Exception if an error occurs during the transaction
     */
    public static void save(EntityManager em, Paquete paquete) {
        try {
            em.getTransaction().begin();
            em.persist(paquete);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e; // Relanzar la excepción para que el llamador la maneje
        }
    }

    /**
     * Finds a Paquete entity by its ID.
     *
     * @param em  the EntityManager to use for the operation
     * @param id  the ID of the Paquete to find
     * @return an Optional containing the Paquete if found, or an empty Optional if not found
     */
    public static Optional<Paquete> findById(EntityManager em, Long id) {
        return Optional.ofNullable(em.find(Paquete.class, id));
    }

    /**
     * Retrieves all Paquete entities from the database.
     *
     * @param em the EntityManager to use for the operation
     * @return a List of all Paquete entities
     */
    public static List<Paquete> getAll(EntityManager em) {
        return em.createQuery("SELECT p FROM Paquete p", Paquete.class).getResultList();
    }

    /**
     * Finds a Paquete entity by its name.
     *
     * @param em   the EntityManager to use for the operation
     * @param name the name of the Paquete to find
     * @return an Optional containing the Paquete if found, or an empty Optional if not found
     */
    public static Optional<Paquete> findByName(EntityManager em, String name) {
        try {
            Paquete paquete = em.createQuery("SELECT p FROM Paquete p WHERE p.name = :name", Paquete.class)
                                .setParameter("name", name)
                                .getSingleResult();
            return Optional.of(paquete);
        } catch (jakarta.persistence.NoResultException e) {
            return Optional.empty();
        }
    }
}

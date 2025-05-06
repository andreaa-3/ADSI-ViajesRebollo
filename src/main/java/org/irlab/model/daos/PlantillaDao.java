package org.irlab.model.daos;

import org.irlab.model.entities.Plantilla;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

/**
 * DAO class for managing Plantilla entities.
 */
public class PlantillaDao {

    /**
     * Saves a new Plantilla entity to the database.
     *
     * @param em        the EntityManager to use for the operation
     * @param plantilla the Plantilla entity to save
     * @throws Exception if an error occurs during the transaction
     */
    public static void save(EntityManager em, Plantilla plantilla) {
        try {
            em.getTransaction().begin();
            em.persist(plantilla);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e; // Relanzar la excepción para que el llamador la maneje
        }
    }

    /**
     * Finds a Plantilla entity by its ID.
     *
     * @param em  the EntityManager to use for the operation
     * @param id  the ID of the Plantilla to find
     * @return an Optional containing the Plantilla if found, or an empty Optional if not found
     */
    public static Optional<Plantilla> findById(EntityManager em, Long id) {
        return Optional.ofNullable(em.find(Plantilla.class, id));
    }

    /**
     * Finds a Plantilla entity by its name.
     *
     * @param em   the EntityManager to use for the operation
     * @param name the name of the Plantilla to find
     * @return an Optional containing the Plantilla if found, or an empty Optional if not found
     */
    public static Optional<Plantilla> findByName(EntityManager em, String name) {
        try {
            Plantilla plantilla = em.createQuery("SELECT p FROM Plantilla p WHERE p.name = :name", Plantilla.class)
                                    .setParameter("name", name)
                                    .getSingleResult();
            return Optional.of(plantilla);
        } catch (jakarta.persistence.NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * Retrieves all Plantilla entities from the database.
     *
     * @param em the EntityManager to use for the operation
     * @return a List of all Plantilla entities
     */
    public static List<Plantilla> getAll(EntityManager em) {
        return em.createQuery("SELECT p FROM Plantilla p", Plantilla.class).getResultList();
    }
}

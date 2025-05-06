package org.irlab.model.daos;

import java.util.List;
import java.util.Optional;

import org.irlab.model.entities.Plan;

import jakarta.persistence.EntityManager;

/**
 * DAO class for managing Plan entities.
 */
public class PlanDao {

    /**
     * Saves a new Plan entity to the database.
     *
     * @param em   the EntityManager to use for the operation
     * @param plan the Plan entity to save
     * @throws Exception if an error occurs during the transaction
     */
    public static void save(EntityManager em, Plan plan) {
        try {
            em.getTransaction().begin();
            em.persist(plan);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e; // Relanzar la excepción para que el llamador la maneje
        }
    }

    /**
     * Finds a Plan entity by its ID.
     *
     * @param em  the EntityManager to use for the operation
     * @param id  the ID of the Plan to find
     * @return an Optional containing the Plan if found, or an empty Optional if not found
     */
    public static Optional<Plan> findById(EntityManager em, Long id) {
        return Optional.ofNullable(em.find(Plan.class, id));
    }

    /**
     * Finds a Plan entity by its name.
     *
     * @param em   the EntityManager to use for the operation
     * @param name the name of the Plan to find
     * @return an Optional containing the Plan if found, or an empty Optional if not found
     */
    public static Optional<Plan> findByName(EntityManager em, String name) {
        try {
            Plan plan = em.createQuery("SELECT p FROM Plan p WHERE p.name = :name", Plan.class)
                          .setParameter("name", name)
                          .getSingleResult();
            return Optional.of(plan);
        } catch (jakarta.persistence.NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * Retrieves all Plan entities from the database.
     *
     * @param em the EntityManager to use for the operation
     * @return a List of all Plan entities
     */
    public static List<Plan> getAll(EntityManager em) {
        return em.createQuery("SELECT p FROM Plan p", Plan.class).getResultList();
    }

}

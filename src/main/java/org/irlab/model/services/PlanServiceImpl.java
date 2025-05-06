package org.irlab.model.services;

import javax.annotation.Nonnull;
import java.util.List;

import org.irlab.common.AppEntityManagerFactory;
import org.irlab.model.daos.PlanDao;
import org.irlab.model.entities.Plan;
import org.irlab.model.exceptions.PlanAlreadyExistsException;
import org.irlab.model.exceptions.PlanInvalidInheritanceException; // Nueva excepción personalizada
import org.irlab.model.exceptions.PlanNotFoundException;

/**
 * Implementation of the PlanService interface.
 */
public class PlanServiceImpl implements PlanService {

    @Override
    public void createPlan(@Nonnull Plan plan) throws PlanAlreadyExistsException, PlanInvalidInheritanceException {
        try (var em = AppEntityManagerFactory.getInstance().createEntityManager()) {
            // Validar si el plan ya existe
            var maybePlan = PlanDao.findByName(em, plan.getName());
            if (maybePlan.isPresent()) {
                throw new PlanAlreadyExistsException("A plan with the name '" + plan.getName() + "' already exists.");
            }

            // Validar que el plan no tenga valores para ambos: paquete y plantilla
            if (plan.getPaqueteBase() != null && plan.getPlantillaBase() != null) {
                throw new PlanInvalidInheritanceException("A plan cannot inherit from both a paquete and a plantilla.");
            }

            // Persistir el plan
            try {
                em.getTransaction().begin();
                em.persist(plan);
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
                throw e;
            }
        }
    }

    @Override
    public Plan getPlanByName(@Nonnull String name) throws PlanNotFoundException {
        try (var em = AppEntityManagerFactory.getInstance().createEntityManager()) {
            return PlanDao.findByName(em, name)
                    .orElseThrow(() -> new PlanNotFoundException("No plan found with the name '" + name + "'."));
        }
    }

    @Override
    public List<Plan> getAllPlans() {
        try (var em = AppEntityManagerFactory.getInstance().createEntityManager()) {
            return PlanDao.getAll(em);
        }
    }
}
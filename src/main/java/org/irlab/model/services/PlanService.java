package org.irlab.model.services;

import javax.annotation.Nonnull;
import java.util.List;
import org.irlab.model.entities.Plan;
import org.irlab.model.exceptions.PlanNotFoundException;
import org.irlab.model.exceptions.PlanAlreadyExistsException;
import org.irlab.model.exceptions.PlanInvalidInheritanceException;

/**
 * The plan service facade
 */
public interface PlanService {

    /**
     * Create a new plan
     *
     * @param plan the plan to create
     * @throws PlanAlreadyExistsException if a plan with the same name already exists
     * @throws PlanInvalidInheritanceException 
     */
    void createPlan(@Nonnull Plan plan) throws PlanAlreadyExistsException, PlanInvalidInheritanceException;

    /**
     * Get a plan by its name
     *
     * @param name the name of the plan
     * @return the plan
     * @throws PlanNotFoundException if no plan with the given name is found
     */
    Plan getPlanByName(@Nonnull String name) throws PlanNotFoundException;

    /**
     * Get all plans
     *
     * @return a list of all plans
     */
    List<Plan> getAllPlans();
}
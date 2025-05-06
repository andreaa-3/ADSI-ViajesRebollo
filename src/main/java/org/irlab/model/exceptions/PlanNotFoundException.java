package org.irlab.model.exceptions;

/**
 * Exception thrown when a plan is not found.
 */
public class PlanNotFoundException extends Exception {

    /**
     * Constructs a new PlanNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public PlanNotFoundException(String message) {
        super(message);
    }
}
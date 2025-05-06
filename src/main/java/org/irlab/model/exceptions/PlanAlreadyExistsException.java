package org.irlab.model.exceptions;

/**
 * Exception thrown when a plan with the same name already exists.
 */
public class PlanAlreadyExistsException extends Exception {

    /**
     * Constructs a new PlanAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message
     */
    public PlanAlreadyExistsException(String message) {
        super(message);
    }
}
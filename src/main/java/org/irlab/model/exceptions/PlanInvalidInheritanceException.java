package org.irlab.model.exceptions;

/**
 * Exception thrown when a plan tries to inherit from both a paquete and a plantilla.
 */
public class PlanInvalidInheritanceException extends Exception {

    /**
     * Constructs a new PlanInvalidInheritanceException with the specified detail message.
     *
     * @param message the detail message
     */
    public PlanInvalidInheritanceException(String message) {
        super(message);
    }
}
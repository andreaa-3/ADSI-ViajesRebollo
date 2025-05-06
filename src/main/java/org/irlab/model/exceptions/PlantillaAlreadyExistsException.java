package org.irlab.model.exceptions;

/**
 * Exception thrown when a plantilla with the same name already exists.
 */
public class PlantillaAlreadyExistsException extends Exception {

    /**
     * Constructs a new PlantillaAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message
     */
    public PlantillaAlreadyExistsException(String message) {
        super(message);
    }
}
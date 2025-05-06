package org.irlab.model.exceptions;

/**
 * Exception thrown when a plantilla is not found.
 */
public class PlantillaNotFoundException extends Exception {

    /**
     * Constructs a new PlantillaNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public PlantillaNotFoundException(String message) {
        super(message);
    }
}
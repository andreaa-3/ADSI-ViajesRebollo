package org.irlab.model.exceptions;

/**
 * Exception thrown when a paquete with the same name already exists.
 */
public class PaqueteAlreadyExistsException extends Exception {

    /**
     * Constructs a new PaqueteAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message
     */
    public PaqueteAlreadyExistsException(String message) {
        super(message);
    }
}
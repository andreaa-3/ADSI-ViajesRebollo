package org.irlab.model.exceptions;

/**
 * Exception thrown when a paquete is not found.
 */
public class PaqueteNotFoundException extends Exception {

    /**
     * Constructs a new PaqueteNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public PaqueteNotFoundException(String message) {
        super(message);
    }
}
package org.irlab.model.services;

import javax.annotation.Nonnull;
import java.util.List;
import org.irlab.model.entities.Paquete;
import org.irlab.model.exceptions.PaqueteNotFoundException;
import org.irlab.model.exceptions.PaqueteAlreadyExistsException;

/**
 * The paquete service facade
 */
public interface PaqueteService {
    /**
     * Create a new paquete
     *
     * @param paquete the paquete to create
     * @throws PaqueteAlreadyExistsException if a paquete with the same name already exists
     */
    void createPaquete(@Nonnull Paquete paquete) throws PaqueteAlreadyExistsException;

    /**
     * Get a paquete by its name
     *
     * @param name the name of the paquete
     * @return the paquete
     * @throws PaqueteNotFoundException if no paquete with the given name is found
     */
    Paquete getPaqueteByName(@Nonnull String name) throws PaqueteNotFoundException;

    /**
     * Get all paquetes
     *
     * @return a list of all paquetes
     */
    List<Paquete> getAllPaquetes();
}
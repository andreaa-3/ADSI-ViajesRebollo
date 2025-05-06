package org.irlab.model.services;

import javax.annotation.Nonnull;
import java.util.List;
import org.irlab.model.entities.Plantilla;
import org.irlab.model.exceptions.PlantillaNotFoundException;
import org.irlab.model.exceptions.PlantillaAlreadyExistsException;

/**
 * The plantilla service facade
 */
public interface PlantillaService {

    /**
     * Create a new plantilla
     *
     * @param plantilla the plantilla to create
     * @throws PlantillaAlreadyExistsException if a plantilla with the same name already exists
     */
    void createPlantilla(@Nonnull Plantilla plantilla) throws PlantillaAlreadyExistsException;

    /**
     * Get a plantilla by its name
     *
     * @param name the name of the plantilla
     * @return the plantilla
     * @throws PlantillaNotFoundException if no plantilla with the given name is found
     */
    Plantilla getPlantillaByName(@Nonnull String name) throws PlantillaNotFoundException;

    /**
     * Get all plantillas
     *
     * @return a list of all plantillas
     */
    List<Plantilla> getAllPlantillas();
}
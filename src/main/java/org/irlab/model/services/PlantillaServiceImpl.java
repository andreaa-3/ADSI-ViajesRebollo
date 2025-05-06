package org.irlab.model.services;

import javax.annotation.Nonnull;
import java.util.List;

import org.irlab.common.AppEntityManagerFactory;
import org.irlab.model.daos.PlantillaDao;
import org.irlab.model.entities.Plantilla;
import org.irlab.model.exceptions.PlantillaAlreadyExistsException;
import org.irlab.model.exceptions.PlantillaNotFoundException;

/**
 * Implementation of the PlantillaService interface.
 */
public class PlantillaServiceImpl implements PlantillaService {

    PlantillaServiceImpl() {
    }

    @Override
    public void createPlantilla(@Nonnull Plantilla plantilla) throws PlantillaAlreadyExistsException {
        try (var em = AppEntityManagerFactory.getInstance().createEntityManager()) {
            var maybePlantilla = PlantillaDao.findByName(em, plantilla.getName());
            if (maybePlantilla.isPresent()) {
                throw new PlantillaAlreadyExistsException("A plantilla with the name '" + plantilla.getName() + "' already exists.");
            }

            try {
                em.getTransaction().begin();
                em.persist(plantilla);
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
                throw e;
            }
        }
    }

    @Override
    public Plantilla getPlantillaByName(@Nonnull String name) throws PlantillaNotFoundException {
        try (var em = AppEntityManagerFactory.getInstance().createEntityManager()) {
            return PlantillaDao.findByName(em, name)
                    .orElseThrow(() -> new PlantillaNotFoundException("No plantilla found with the name '" + name + "'."));
        }
    }

    @Override
    public List<Plantilla> getAllPlantillas() {
        try (var em = AppEntityManagerFactory.getInstance().createEntityManager()) {
            return PlantillaDao.getAll(em);
        }
    }
}
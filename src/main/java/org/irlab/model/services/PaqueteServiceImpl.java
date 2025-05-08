package org.irlab.model.services;

import javax.annotation.Nonnull;

import java.util.List;

import org.irlab.common.AppEntityManagerFactory;
import org.irlab.model.daos.PaqueteDao;
import org.irlab.model.entities.Paquete;
import org.irlab.model.exceptions.PaqueteNotFoundException;
import org.irlab.model.exceptions.PaqueteAlreadyExistsException;


/**
 * Implementation of the PaqueteService interface.
 */
public class PaqueteServiceImpl implements PaqueteService {

    public PaqueteServiceImpl() {
    }
    
    @Override
    public void createPaquete(@Nonnull Paquete paquete) throws PaqueteAlreadyExistsException {
        try (var em = AppEntityManagerFactory.getInstance().createEntityManager()) {
            var maybePaquete = PaqueteDao.findByName(em, paquete.getName());
            if (maybePaquete.isPresent()) {
                throw new PaqueteAlreadyExistsException("A paquete with the name '" + paquete.getName() + "' already exists.");
            }

            try {
                em.getTransaction().begin();
                em.persist(paquete);
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
                throw e; // Relanzar la excepción para que el llamador la maneje
            }
        }
    }

    @Override
    public Paquete getPaqueteByName(@Nonnull String name) throws PaqueteNotFoundException {
        try (var em = AppEntityManagerFactory.getInstance().createEntityManager()) {
            return PaqueteDao.findByName(em, name)
                    .orElseThrow(() -> new PaqueteNotFoundException("No paquete found with the name '" + name + "'.")); 
        }
    }

    @Override
    public List<Paquete> getAllPaquetes() {
        try (var em = AppEntityManagerFactory.getInstance().createEntityManager()) {
            return PaqueteDao.getAll(em);
        }
    }
}
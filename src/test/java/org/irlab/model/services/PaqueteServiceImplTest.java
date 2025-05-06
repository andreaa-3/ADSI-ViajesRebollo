package org.irlab.model.services;

import org.irlab.common.AppEntityManagerFactory;
import org.irlab.model.entities.Paquete;
import org.irlab.model.exceptions.PaqueteAlreadyExistsException;
import org.irlab.model.exceptions.PaqueteNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaqueteServiceImplTest {

    private PaqueteServiceImpl paqueteService;

    @BeforeEach
    void setUp() {
        paqueteService = new PaqueteServiceImpl();
        limpiarBaseDeDatos();
    }

    // Ojo con esto !, creo puede eliminar los datos insertados por los 
        // scripts sql de inicialización de la base de datos.

    private void limpiarBaseDeDatos() {
        try (var em = AppEntityManagerFactory.getInstance().createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Paquete").executeUpdate();
            em.getTransaction().commit();
        }
    }

    
    @Test
    void testCreatePaquete_Success() {
        Paquete paquete = new Paquete();
        paquete.setName("Paquete Aventura");
        paquete.setDescription("Un paquete lleno de aventuras");
        paquete.setStartDate(LocalDate.of(2025, 6, 1));
        paquete.setEndDate(LocalDate.of(2025, 6, 10));
        paquete.setRequiredPeople(4);
        paquete.setPrice(1200.50);

        assertDoesNotThrow(() -> paqueteService.createPaquete(paquete));
    }

    @Test
    void testCreatePaquete_AlreadyExists() {
        Paquete paquete = new Paquete();
        paquete.setName("Paquete Aventura");
        paquete.setDescription("Un paquete lleno de aventuras");
        paquete.setStartDate(LocalDate.of(2025, 6, 1));
        paquete.setEndDate(LocalDate.of(2025, 6, 10));
        paquete.setRequiredPeople(4);
        paquete.setPrice(1200.50);

        // Crear el paquete por primera vez
        assertDoesNotThrow(() -> paqueteService.createPaquete(paquete));

        // Intentar crearlo nuevamente debería lanzar una excepción
        assertThrows(PaqueteAlreadyExistsException.class, () -> paqueteService.createPaquete(paquete));
    }

    @Test
    void testGetPaqueteByName_Success() throws PaqueteNotFoundException, PaqueteAlreadyExistsException {
        Paquete paquete = new Paquete();
        paquete.setName("Paquete Relax");
        paquete.setDescription("Un paquete para relajarse");
        paquete.setStartDate(LocalDate.of(2025, 7, 15));
        paquete.setEndDate(LocalDate.of(2025, 7, 25));
        paquete.setRequiredPeople(2);
        paquete.setPrice(2500.00);

        paqueteService.createPaquete(paquete);

        Paquete foundPaquete = paqueteService.getPaqueteByName("Paquete Relax");
        assertNotNull(foundPaquete);
        assertEquals("Paquete Relax", foundPaquete.getName());
    }

    @Test
    void testGetPaqueteByName_NotFound() {
        assertThrows(PaqueteNotFoundException.class, () -> paqueteService.getPaqueteByName("Paquete Inexistente"));
    }

    @Test
    void testGetAllPaquetes() throws PaqueteAlreadyExistsException {
        Paquete paquete1 = new Paquete();
        paquete1.setName("Paquete Aventura");
        paquete1.setDescription("Un paquete lleno de aventuras");
        paquete1.setStartDate(LocalDate.of(2025, 6, 1));
        paquete1.setEndDate(LocalDate.of(2025, 6, 10));
        paquete1.setRequiredPeople(4);
        paquete1.setPrice(1200.50);

        Paquete paquete2 = new Paquete();
        paquete2.setName("Paquete Relax");
        paquete2.setDescription("Un paquete para relajarse");
        paquete2.setStartDate(LocalDate.of(2025, 7, 15));
        paquete2.setEndDate(LocalDate.of(2025, 7, 25));
        paquete2.setRequiredPeople(2);
        paquete2.setPrice(2500.00);

        paqueteService.createPaquete(paquete1);
        paqueteService.createPaquete(paquete2);

        List<Paquete> paquetes = paqueteService.getAllPaquetes();
        assertEquals(2, paquetes.size());
    }
}
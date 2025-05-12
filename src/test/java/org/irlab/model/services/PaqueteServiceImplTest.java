package org.irlab.model.services;

import org.irlab.common.AppEntityManagerFactory;
import org.irlab.model.entities.Paquete;
import org.irlab.model.exceptions.PaqueteAlreadyExistsException;
import org.irlab.model.exceptions.PaqueteNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaqueteServiceImplTest {

    private PaqueteServiceImpl paqueteService;
    private List<String> paquetesCreados; // Lista para rastrear los nombres de los paquetes creados

    @BeforeEach
    void setUp() {
        paqueteService = new PaqueteServiceImpl();
        paquetesCreados = new ArrayList<>();
    }

    @AfterEach
    void limpiarPaquetesCreados() {
        try (var em = AppEntityManagerFactory.getInstance().createEntityManager()) {
            em.getTransaction().begin();
            for (String paqueteName : paquetesCreados) {
                em.createQuery("DELETE FROM Paquete p WHERE p.name = :name")
                  .setParameter("name", paqueteName)
                  .executeUpdate();
            }
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
        paquetesCreados.add(paquete.getName()); // Registrar el paquete creado
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
        paquetesCreados.add(paquete.getName()); // Registrar el paquete creado

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
        paquetesCreados.add(paquete.getName()); // Registrar el paquete creado

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
        paquete1.setName("Paquete_XYZ123_Aventura");
        paquete1.setDescription("Un paquete lleno de aventuras");
        paquete1.setStartDate(LocalDate.of(2025, 6, 1));
        paquete1.setEndDate(LocalDate.of(2025, 6, 10));
        paquete1.setRequiredPeople(4);
        paquete1.setPrice(1200.50);

        Paquete paquete2 = new Paquete();
        paquete2.setName("Paquete_ABC987_Relax");
        paquete2.setDescription("Un paquete para relajarse");
        paquete2.setStartDate(LocalDate.of(2025, 7, 15));
        paquete2.setEndDate(LocalDate.of(2025, 7, 25));
        paquete2.setRequiredPeople(2);
        paquete2.setPrice(2500.00);

        paqueteService.createPaquete(paquete1);
        paqueteService.createPaquete(paquete2);

        paquetesCreados.add(paquete1.getName()); // Registrar el paquete creado
        paquetesCreados.add(paquete2.getName()); // Registrar el paquete creado

        // Obtener todos los paquetes y filtrar por los nombres creados en este test
        List<Paquete> paquetes = paqueteService.getAllPaquetes();
        List<Paquete> paquetesFiltrados = paquetes.stream()
            .filter(p -> paquetesCreados.contains(p.getName()))
            .toList();

        assertEquals(2, paquetesFiltrados.size());
    }
}
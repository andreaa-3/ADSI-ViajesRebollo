package org.irlab.model.services;

import org.irlab.common.AppEntityManagerFactory;
import org.irlab.model.entities.Plantilla;
import org.irlab.model.exceptions.PlantillaAlreadyExistsException;
import org.irlab.model.exceptions.PlantillaNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlantillaServiceImplTest {

    private PlantillaServiceImpl plantillaService;
    private List<String> plantillasCreadas; // Lista para rastrear los nombres de las plantillas creadas

    @BeforeEach
    void setUp() {
        plantillaService = new PlantillaServiceImpl();
        plantillasCreadas = new ArrayList<>();
    }

    @AfterEach
    void limpiarPlantillasCreadas() {
        try (var em = AppEntityManagerFactory.getInstance().createEntityManager()) {
            em.getTransaction().begin();
            for (String plantillaName : plantillasCreadas) {
                em.createQuery("DELETE FROM Plantilla p WHERE p.name = :name")
                  .setParameter("name", plantillaName)
                  .executeUpdate();
            }
            em.getTransaction().commit();
        }
    }

    @Test
    void testCreatePlantilla_Success() {
        Plantilla plantilla = new Plantilla();
        plantilla.setName("Plantilla Aventura");
        plantilla.setDescription("Una plantilla llena de aventuras");
        plantilla.setDestination("Montañas");
        plantilla.setAccommodation("Hotel 5 estrellas");

        assertDoesNotThrow(() -> plantillaService.createPlantilla(plantilla));
        plantillasCreadas.add(plantilla.getName()); // Registrar la plantilla creada
    }

    @Test
    void testCreatePlantilla_AlreadyExists() {
        Plantilla plantilla = new Plantilla();
        plantilla.setName("Plantilla Aventura");
        plantilla.setDescription("Una plantilla llena de aventuras");
        plantilla.setDestination("Montañas");
        plantilla.setAccommodation("Hotel 5 estrellas");

        // Crear la plantilla por primera vez
        assertDoesNotThrow(() -> plantillaService.createPlantilla(plantilla));
        plantillasCreadas.add(plantilla.getName()); // Registrar la plantilla creada

        // Intentar crearla nuevamente debería lanzar una excepción
        assertThrows(PlantillaAlreadyExistsException.class, () -> plantillaService.createPlantilla(plantilla));
    }

    @Test
    void testGetPlantillaByName_Success() throws PlantillaNotFoundException, PlantillaAlreadyExistsException {
        Plantilla plantilla = new Plantilla();
        plantilla.setName("Plantilla Relax");
        plantilla.setDescription("Una plantilla para relajarse");
        plantilla.setDestination("Playa");
        plantilla.setAccommodation("Resort todo incluido");

        plantillaService.createPlantilla(plantilla);
        plantillasCreadas.add(plantilla.getName()); // Registrar la plantilla creada

        Plantilla foundPlantilla = plantillaService.getPlantillaByName("Plantilla Relax");
        assertNotNull(foundPlantilla);
        assertEquals("Plantilla Relax", foundPlantilla.getName());
    }

    @Test
    void testGetPlantillaByName_NotFound() {
        // No se crea ninguna plantilla, por lo que no es necesario registrar nada en plantillasCreadas
        assertThrows(PlantillaNotFoundException.class, () -> plantillaService.getPlantillaByName("Plantilla Inexistente"));
    }

    @Test
    void testGetAllPlantillas() throws PlantillaAlreadyExistsException {
        Plantilla plantilla1 = new Plantilla();
        plantilla1.setName("Plantilla_UNIQUE123_Aventura");
        plantilla1.setDescription("Una plantilla llena de aventuras");
        plantilla1.setDestination("Montañas");
        plantilla1.setAccommodation("Hotel 5 estrellas");

        Plantilla plantilla2 = new Plantilla();
        plantilla2.setName("Plantilla_UNIQUE456_Relax");
        plantilla2.setDescription("Una plantilla para relajarse");
        plantilla2.setDestination("Playa");
        plantilla2.setAccommodation("Resort todo incluido");

        plantillaService.createPlantilla(plantilla1);
        plantillaService.createPlantilla(plantilla2);

        plantillasCreadas.add(plantilla1.getName()); // Registrar la plantilla creada
        plantillasCreadas.add(plantilla2.getName()); // Registrar la plantilla creada

        // Obtener todas las plantillas y filtrar por los nombres creados en este test
        List<Plantilla> plantillas = plantillaService.getAllPlantillas();
        List<Plantilla> plantillasFiltradas = plantillas.stream()
            .filter(p -> plantillasCreadas.contains(p.getName()))
            .toList();

        assertEquals(2, plantillasFiltradas.size());
    }
}
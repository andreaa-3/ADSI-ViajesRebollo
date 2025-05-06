package org.irlab.model.services;

import org.irlab.common.AppEntityManagerFactory;
import org.irlab.model.entities.Paquete;
import org.irlab.model.entities.Plan;
import org.irlab.model.entities.Plantilla;
import org.irlab.model.exceptions.PlanAlreadyExistsException;
import org.irlab.model.exceptions.PlanInvalidInheritanceException;
import org.irlab.model.exceptions.PlanNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlanServiceImplTest {

    private PlanServiceImpl planService;

    @BeforeEach
    void setUp() {
        planService = new PlanServiceImpl();
        limpiarBaseDeDatos();
    }

    private void limpiarBaseDeDatos() {
        try (var em = AppEntityManagerFactory.getInstance().createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Plan").executeUpdate();
            em.getTransaction().commit();
        }
    }

    @Test
    void testCreatePlan_Success() {
        Plan plan = new Plan();
        plan.setName("Plan Aventura");
        plan.setDescription("Un plan lleno de aventuras");
        plan.setDestination(List.of("Montañas", "Bosque"));
        plan.setActivities(List.of("Senderismo", "Escalada"));
        plan.setStartDate(LocalDate.of(2025, 6, 1));
        plan.setEndDate(LocalDate.of(2025, 6, 10));
        plan.setAccommodation(List.of("Hotel 5 estrellas", "Cabaña"));
        plan.setTransportation(List.of("Autobús", "Camioneta"));
        plan.setPrice(1500.00);

        assertDoesNotThrow(() -> planService.createPlan(plan));
    }

    @Test
    void testCreatePlan_AlreadyExists() {
        Plan plan = new Plan();
        plan.setName("Plan Aventura");
        plan.setDescription("Un plan lleno de aventuras");
        plan.setDestination(List.of("Montañas", "Bosque"));
        plan.setActivities(List.of("Senderismo", "Escalada"));
        plan.setStartDate(LocalDate.of(2025, 6, 1));
        plan.setEndDate(LocalDate.of(2025, 6, 10));
        plan.setAccommodation(List.of("Hotel 5 estrellas", "Cabaña"));
        plan.setTransportation(List.of("Autobús", "Camioneta"));
        plan.setPrice(1500.00);

        // Crear el plan por primera vez
        assertDoesNotThrow(() -> planService.createPlan(plan));

        // Intentar crearlo nuevamente debería lanzar una excepción
        assertThrows(PlanAlreadyExistsException.class, () -> planService.createPlan(plan));
    }

    @Test
    void testGetPlanByName_Success() throws PlanNotFoundException, PlanAlreadyExistsException, PlanInvalidInheritanceException {
        Plan plan = new Plan();
        plan.setName("Plan Relax");
        plan.setDescription("Un plan para relajarse");
        plan.setDestination(List.of("Playa", "Isla"));
        plan.setActivities(List.of("Natación", "Yoga"));
        plan.setStartDate(LocalDate.of(2025, 7, 1));
        plan.setEndDate(LocalDate.of(2025, 7, 10));
        plan.setAccommodation(List.of("Resort todo incluido", "Villa privada"));
        plan.setTransportation(List.of("Avión", "Lancha"));
        plan.setPrice(2500.00);

        planService.createPlan(plan);

        Plan foundPlan = planService.getPlanByName("Plan Relax");
        assertNotNull(foundPlan);
        assertEquals("Plan Relax", foundPlan.getName());
        assertEquals(List.of("Playa", "Isla"), foundPlan.getDestination());
        assertEquals(List.of("Natación", "Yoga"), foundPlan.getActivities());
        assertEquals(LocalDate.of(2025, 7, 1), foundPlan.getStartDate());
        assertEquals(LocalDate.of(2025, 7, 10), foundPlan.getEndDate());
        assertEquals(List.of("Resort todo incluido", "Villa privada"), foundPlan.getAccommodation());
        assertEquals(List.of("Avión", "Lancha"), foundPlan.getTransportation());
        assertEquals(2500.00, foundPlan.getPrice());
    }

    @Test
    void testGetPlanByName_NotFound() {
        assertThrows(PlanNotFoundException.class, () -> planService.getPlanByName("Plan Inexistente"));
    }

    @Test
    void testGetAllPlans() throws PlanAlreadyExistsException, PlanInvalidInheritanceException {
        Plan plan1 = new Plan();
        plan1.setName("Plan Aventura");
        plan1.setDescription("Un plan lleno de aventuras");
        plan1.setDestination(List.of("Montañas", "Bosque"));
        plan1.setActivities(List.of("Senderismo", "Escalada"));
        plan1.setStartDate(LocalDate.of(2025, 6, 1));
        plan1.setEndDate(LocalDate.of(2025, 6, 10));
        plan1.setAccommodation(List.of("Hotel 5 estrellas", "Cabaña"));
        plan1.setTransportation(List.of("Autobús", "Camioneta"));
        plan1.setPrice(1500.00);

        Plan plan2 = new Plan();
        plan2.setName("Plan Relax");
        plan2.setDescription("Un plan para relajarse");
        plan2.setDestination(List.of("Playa", "Isla"));
        plan2.setActivities(List.of("Natación", "Yoga"));
        plan2.setStartDate(LocalDate.of(2025, 7, 1));
        plan2.setEndDate(LocalDate.of(2025, 7, 10));
        plan2.setAccommodation(List.of("Resort todo incluido", "Villa privada"));
        plan2.setTransportation(List.of("Avión", "Lancha"));
        plan2.setPrice(2500.00);

        planService.createPlan(plan1);
        planService.createPlan(plan2);

        List<Plan> plans = planService.getAllPlans();
        assertEquals(2, plans.size());
    }

    @Test
    void testCreatePlan_InvalidInheritance() {
        Plan plan = new Plan();
        plan.setName("Plan Mixto");
        plan.setDescription("Un plan que intenta heredar de ambos");
        plan.setPaqueteBase(new Paquete()); // Simula que tiene un paquete base
        plan.setPlantillaBase(new Plantilla()); // Simula que tiene una plantilla base

        // Intentar crear el plan debería lanzar una excepción
        assertThrows(PlanInvalidInheritanceException.class, () -> planService.createPlan(plan));
    }
}
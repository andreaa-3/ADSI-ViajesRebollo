package org.irlab;

import org.irlab.common.AppEntityManagerFactory;
import org.irlab.model.entities.Paquete;
import org.irlab.model.entities.Plantilla;
import org.irlab.model.entities.Plan;
import org.irlab.model.exceptions.*;
import org.irlab.model.services.*;

import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class App {

    private enum Command {
        CREATE_PAQUETE, CREATE_PLANTILLA, CREATE_PLAN, EXIT
    }

    private static PaqueteService paqueteService = new PaqueteServiceImpl();
    private static PlantillaService plantillaService = new PlantillaServiceImpl();
    private static PlanService planService = new PlanServiceImpl();
    private static Scanner scanner = new Scanner(System.in);

    private static void init() {
        try (EntityManager em = AppEntityManagerFactory.getInstance().createEntityManager()) {
            // Inicialización de la base de datos si es necesario
        }
    }

    private static void shutdown() {
        AppEntityManagerFactory.close();
        scanner.close();
    }

    private static Command getCommand() {
        System.out.println("Choose an option:");
        System.out.println("  1) Create Paquete");
        System.out.println("  2) Create Plantilla");
        System.out.println("  3) Create Plan Específico");
        System.out.println("  q) Exit");
        while (true) {
            System.out.print("Option: ");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    return Command.CREATE_PAQUETE;
                case "2":
                    return Command.CREATE_PLANTILLA;
                case "3":
                    return Command.CREATE_PLAN;
                case "q":
                    return Command.EXIT;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void createPaquete() {
        System.out.print("Enter Paquete name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Paquete description: ");
        String description = scanner.nextLine();
        System.out.print("Enter Paquete destinations (comma-separated): ");
        String destinations = scanner.nextLine();
        System.out.print("Enter Paquete activities (comma-separated): ");
        String activities = scanner.nextLine();
        System.out.print("Enter Paquete start date (yyyy-mm-dd): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter Paquete end date (yyyy-mm-dd): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter Paquete price: ");
        double price = Double.parseDouble(scanner.nextLine());

        Paquete paquete = new Paquete();
        paquete.setName(name);
        paquete.setDescription(description);
        paquete.setDestination(List.of(destinations.split(",")));
        paquete.setActivities(List.of(activities.split(",")));
        paquete.setStartDate(startDate);
        paquete.setEndDate(endDate);
        paquete.setPrice(price);

        try {
            paqueteService.createPaquete(paquete);
            System.out.println("Paquete created successfully.");
        } catch (PaqueteAlreadyExistsException e) {
            System.out.println("Error: A Paquete with this name already exists.");
        }
    }

    private static void createPlantilla() {
        System.out.print("Enter Plantilla name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Plantilla description: ");
        String description = scanner.nextLine();
        System.out.print("Enter Plantilla destinations (comma-separated): ");
        String destinations = scanner.nextLine();
        System.out.print("Enter Plantilla accommodations (comma-separated): ");
        String accommodations = scanner.nextLine();

        Plantilla plantilla = new Plantilla();
        plantilla.setName(name);
        plantilla.setDescription(description);
        plantilla.setDestination(destinations);
        plantilla.setAccommodation(accommodations);

        try {
            plantillaService.createPlantilla(plantilla);
            System.out.println("Plantilla created successfully.");
        } catch (PlantillaAlreadyExistsException e) {
            System.out.println("Error: A Plantilla with this name already exists.");
        }
    }

    private static void createPlan() {
        System.out.println("Do you want to base the Plan on:");
        System.out.println("  1) A Plantilla");
        System.out.println("  2) A Paquete");
        System.out.print("Option: ");
        String option = scanner.nextLine();

        if ("1".equals(option)) {
            createPlanFromPlantilla();
        } else if ("2".equals(option)) {
            createPlanFromPaquete();
        } else {
            System.out.println("Invalid option.");
        }
    }

    private static void createPlanFromPlantilla() {
        List<Plantilla> plantillas = plantillaService.getAllPlantillas();
        if (plantillas.isEmpty()) {
            System.out.println("No Plantillas available to base the Plan on.");
            return;
        }

        System.out.println("Available Plantillas:");
        for (int i = 0; i < plantillas.size(); i++) {
            System.out.println((i + 1) + ") " + plantillas.get(i).getName());
        }

        System.out.print("Choose a Plantilla: ");
        int choice = Integer.parseInt(scanner.nextLine()) - 1;
        if (choice < 0 || choice >= plantillas.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        Plantilla basePlantilla = plantillas.get(choice);
        Plan plan = new Plan();
        plan.setPlantillaBase(basePlantilla);

        System.out.print("Enter Plan name: ");
        plan.setName(scanner.nextLine());
        System.out.print("Enter Plan description: ");
        plan.setDescription(scanner.nextLine());
        System.out.print("Add extra destinations (comma-separated): ");
        String destinations = scanner.nextLine();
        for (String destination : destinations.split(",")) {
            plan.addDestination(destination.trim());
        }

        try {
            planService.createPlan(plan);
            System.out.println("Plan created successfully.");
        } catch (PlanAlreadyExistsException | PlanInvalidInheritanceException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void createPlanFromPaquete() {
        List<Paquete> paquetes = paqueteService.getAllPaquetes();
        if (paquetes.isEmpty()) {
            System.out.println("No Paquetes available to base the Plan on.");
            return;
        }

        System.out.println("Available Paquetes:");
        for (int i = 0; i < paquetes.size(); i++) {
            System.out.println((i + 1) + ") " + paquetes.get(i).getName());
        }

        System.out.print("Choose a Paquete: ");
        int choice = Integer.parseInt(scanner.nextLine()) - 1;
        if (choice < 0 || choice >= paquetes.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        Paquete basePaquete = paquetes.get(choice);
        Plan plan = new Plan();
        plan.setPaqueteBase(basePaquete);

        System.out.print("Enter Plan name: ");
        plan.setName(scanner.nextLine());
        System.out.print("Enter Plan description: ");
        plan.setDescription(scanner.nextLine());
        System.out.print("Add extra destinations (comma-separated): ");
        String destinations = scanner.nextLine();
        for (String destination : destinations.split(",")) {
            plan.addDestination(destination.trim());
        }

        try {
            planService.createPlan(plan);
            System.out.println("Plan created successfully.");
        } catch (PlanAlreadyExistsException | PlanInvalidInheritanceException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        init();
        boolean exit = false;
        while (!exit) {
            Command command = getCommand();
            switch (command) {
                case CREATE_PAQUETE -> createPaquete();
                case CREATE_PLANTILLA -> createPlantilla();
                case CREATE_PLAN -> createPlan();
                case EXIT -> exit = true;
            }
        }
        shutdown();
    }
}
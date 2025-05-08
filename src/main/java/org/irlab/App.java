package org.irlab;

import org.irlab.common.AppEntityManagerFactory;
import org.irlab.model.entities.Paquete;
import org.irlab.model.entities.Plantilla;
import org.irlab.model.entities.Plan;
import org.irlab.model.exceptions.*;
import org.irlab.model.services.*;

import com.google.common.base.Function;

import jakarta.persistence.EntityManager;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
        System.out.println("  3) Create Specific Plan");
        System.out.println("  q) Exit");
        while (true) {
            System.out.print("Option: ");
            String input = scanner.nextLine();
            switch (input) {
                case "1": return Command.CREATE_PAQUETE;
                case "2": return Command.CREATE_PLANTILLA;
                case "3": return Command.CREATE_PLAN;
                case "q": return Command.EXIT;
                default: System.out.println("Invalid option. Try again.");
            }
        }
    }


    /* DATA FUNCTIONS */
    private static String askSave (String className) {
        System.out.println("Do you want to save the " + className + ":");
        System.out.println("  1) Yes");
        System.out.println("  2) No");
        System.out.print("Option: ");
        return scanner.nextLine();
    }

    private static String introduceString (String className, String fieldName) {
        System.out.print("Enter " + className + " " + fieldName + ": ");
        return scanner.nextLine().trim();
    }
    
    private static boolean validateMandatoryString (String input) {
        return !input.isEmpty();
    }

    private static List<String> introduceList (String className, String fieldName) {
        System.out.print("Enter " + className + " " + fieldName + " (comma-separated): ");
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) return null;
            
        // Clean, filter gaps and remove duplicates while maintaining order.
        Set<String> cleanedSet = Arrays.stream(input.split("\\s*,\\s*"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new));
            
        if (cleanedSet.isEmpty()) return null;
        return new ArrayList<>(cleanedSet);
    }

    private static boolean validateMandatoryList (List<String> list) {
        return list != null;
    }

    private static LocalDate introduceDate (String className, String fieldName, String extra) {
        LocalDate date;

        System.out.print("Enter " + className + " " + fieldName + " (yyyy-mm-dd)" + extra + ": ");
        String input = scanner.nextLine().trim();

        try {
            date = LocalDate.parse(input);
        } catch (DateTimeParseException e) {
            return null;
        }

        return date;
    }

    private static boolean validateMandatoryDate (LocalDate date) {
        return date != null;
    }

    private static boolean validateEndDate (LocalDate endDate, LocalDate startDate, boolean startDateValidated) {
        if (!validateMandatoryDate (endDate)) return false;
        return (!startDateValidated || endDate.isAfter(startDate)); // Si startDate no es correcta, se devuelve true, si no, se comprueba que sea posterior.
    }

    private static double introduceNumber (String className, String fieldName) {
        double n;

        System.out.print("Enter " + className + " " + fieldName + ": ");
        String input = scanner.nextLine().trim();
        try {
            n = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return -1;
        }

        return n;
    }

    private static boolean validateMandatoryPositiveNumber (double n) {
        return n > 0;
    }

    /* PAQUETE FUNCTIONS */

    private static Paquete setPaqueteFieldsByUser (Paquete paquete) {
        if (paquete.getName() == null) paquete.setName(introduceString("Paquete", "name"));
        if (paquete.getDescription() == null) paquete.setDescription(introduceString("Paquete", "description"));
        if (paquete.getDestination() == null) paquete.setDestination(introduceList("Paquete", "destination/s"));
        if (paquete.getAccommodation() == null) paquete.setAccommodation(introduceList("Paquete", "accommodation/s"));
        if (paquete.getTransportation() == null) paquete.setTransportation(introduceList("Paquete", "transportation/s"));
        if (paquete.getActivities() == null) paquete.setActivities(introduceList("Paquete", "activity/ies"));
        if (paquete.getStartDate() == null) paquete.setStartDate(introduceDate("Paquete", "start date", ""));
        if (paquete.getEndDate() == null) paquete.setEndDate(introduceDate("Paquete", "end date", " (must be after start date)"));
        if (paquete.getPrice() == -1) paquete.setPrice(introduceNumber("Paquete", "price"));
        return paquete;
    }

    private static void savePaquete(Paquete paquete) {
        while (true) {
            try {
                paqueteService.createPaquete(paquete);
                System.out.println("Paquete created successfully.");
                break;
            } catch (PaqueteAlreadyExistsException e) {
                System.out.println("Error: " + e.getMessage());
                
                Paquete existent;
                try {
                    existent = paqueteService.getPaqueteByName(paquete.getName());
                    System.out.println("Existent Paquete: " + existent);
                    paquete.setName(introduceString("Paquete", "name"));
                } catch (PaqueteNotFoundException e1) { // It should never happen
                    System.out.println(e1.getMessage());
                }
            }
        }
    }

    private static void createPaquete() {
        boolean vName, vDescription, vDestination, vAccommodation, vTransportation, vActivities, vStartDate, vEndDate, vPrice;
        boolean valid;
        Paquete paquete = new Paquete();

        do {
            paquete = setPaqueteFieldsByUser (paquete);

            // Confirmate
            String option = askSave("Paquete");
            if (option.equals("2")) return;

            // Validate
            vName = validateMandatoryString(paquete.getName());
            vDescription = validateMandatoryString(paquete.getDescription());
            vDestination = validateMandatoryList(paquete.getDestination());
            vAccommodation = validateMandatoryList(paquete.getAccommodation());
            vTransportation = validateMandatoryList(paquete.getTransportation());
            vActivities = validateMandatoryList(paquete.getActivities());
            vStartDate = validateMandatoryDate(paquete.getStartDate());
            vEndDate = validateEndDate(paquete.getEndDate(), paquete.getStartDate(), vStartDate);
            vPrice = validateMandatoryPositiveNumber(paquete.getPrice());

            valid = vName && vDescription && vDestination && vAccommodation && vTransportation &&
                    vActivities && vStartDate && vEndDate && vPrice;

            if (!valid) {
                System.out.println("Some mandatory fields are invalid. Please correct them:");
                if (!vName) paquete.setName(null);
                if (!vDescription) paquete.setDescription(null);
                if (!vDestination) paquete.setDestination(null);
                if (!vAccommodation) paquete.setAccommodation(null);
                if (!vTransportation) paquete.setTransportation(null);
                if (!vActivities) paquete.setActivities(null);
                if (!vStartDate) paquete.setStartDate(null);
                if (!vEndDate) paquete.setEndDate(null);
                if (!vPrice) paquete.setPrice(-1);
            }
        } while (!valid);

        savePaquete(paquete);
    }

    /* PLANTILLA FUNCTIONS */

    private static Plantilla setPlantillaFieldsByUser (Plantilla plantilla) {
        if (plantilla.getName() == null) plantilla.setName(introduceString("Plantilla", "name"));
        if (plantilla.getDescription() == null) plantilla.setDescription(introduceString("Plantilla", "description"));
        if (plantilla.getDestination() == null) plantilla.setDestination(introduceString("Plantilla", "destination/s"));
        if (plantilla.getAccommodation() == null) plantilla.setAccommodation(introduceString("Plantilla", "accommodation/s"));
        if (plantilla.getTransportation() == null) plantilla.setTransportation(introduceList("Plantilla", "transportation/s"));
        if (plantilla.getActivities() == null) plantilla.setActivities(introduceList("Plantilla", "activity/ies"));
        return plantilla;
    }

    private static void savePlantilla(Plantilla plantilla) {
        while (true) {
            try {
                plantillaService.createPlantilla(plantilla);
                System.out.println("Plantilla created successfully.");
                break;
            } catch (PlantillaAlreadyExistsException e) {
                System.out.println("Error: " + e.getMessage());
    
                try {
                    Plantilla existent = plantillaService.getPlantillaByName(plantilla.getName());
                    System.out.println("Existent Plantilla: " + existent);
                    plantilla.setName(introduceString("Plantilla", "name"));
                } catch (PlantillaNotFoundException e1) { // Esto no debería ocurrir
                    System.out.println("Unexpected error: " + e1.getMessage());
                }
            }
        }
    }    

    private static void createPlantilla() {
        boolean vName, vDescription, vDestination, vAccommodation, vTransportation, vActivities;
        boolean valid;
        Plantilla plantilla = new Plantilla();

        do {
            plantilla = setPlantillaFieldsByUser (plantilla);

            // Confirmate
            String option = askSave("Plantilla");
            if (option.equals("2")) return;

            // Validate
            vName = validateMandatoryString(plantilla.getName());
            vDescription = validateMandatoryString(plantilla.getDescription());
            vDestination = validateMandatoryString(plantilla.getDestination());
            vAccommodation = validateMandatoryString(plantilla.getAccommodation());
            vTransportation = validateMandatoryList(plantilla.getTransportation());
            vActivities = validateMandatoryList(plantilla.getActivities());

            valid = vName && vDescription && vDestination && vAccommodation && vTransportation &&
                    vActivities;

            if (!valid) {
                System.out.println("Some mandatory fields are invalid. Please correct them:");
                if (!vName) plantilla.setName(null);
                if (!vDescription) plantilla.setDescription(null);
                if (!vDestination) plantilla.setDestination(null);
                if (!vAccommodation) plantilla.setAccommodation(null);
                if (!vTransportation) plantilla.setTransportation(null);
                if (!vActivities) plantilla.setActivities(null);
            }
        } while (!valid);

        savePlantilla(plantilla);
    }

    /* COMMON PLAN FUNCTIONS */

    private static void createPlan() {
        System.out.println("Do you want to base the Plan on:");
        System.out.println("  1) A Plantilla");
        System.out.println("  2) A Paquete");
        System.out.print("Option: ");
        String option = scanner.nextLine();

        switch (option) {
            case "1" -> createPlanFromPlantilla();
            case "2" -> createPlanFromPaquete();
            default -> System.out.println("Invalid option.");
        }
    }

    private static <T> T chooseFromList (List<T> items, String className, Function<T, String> nameExtractor) {
        if (items.isEmpty()) {
            System.out.println("No " + className + "s available to base the Plan on.");
            return null;
        }
    
        System.out.println("Available " + className + "s:");
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ") " + nameExtractor.apply(items.get(i)));
        }
    
        System.out.print("Choose a " + className + ": ");
        try {
            int choice = Integer.parseInt(scanner.nextLine()) - 1;
            if (choice < 0 || choice >= items.size()) {
                System.out.println("Invalid choice.");
                return null;
            }
            return items.get(choice);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Must be a number.");
            return null;
        }
    }

    private static void printList (String fieldName, List<String> list) {
        System.out.println("Current " + fieldName + ": " + (list != null ? String.join(", ", list) : "none")); // Validación por si acaso, aunque siempre debería haber al menos una.
    }

    private static void introduceExtraList(String fieldName, Consumer<String> addFunction) {
        System.out.print("Add Plan extra " + fieldName + " (comma-separated) or press Enter to skip: ");
        String list = scanner.nextLine().trim();

        if (!list.isEmpty()) {
            Set<String> uniqueItems = Arrays.stream(list.split("\\s*,\\s*"))
                                            .map(String::trim)
                                            .filter(s -> !s.isEmpty())
                                            .collect(Collectors.toCollection(LinkedHashSet::new));
            
            uniqueItems.forEach(addFunction);
        }
    }
    
    private static void savePlan(Plan plan) {
        while (true) {
            try {
                planService.createPlan(plan);
                System.out.println("Plan created successfully.");
                break;
            } catch (PlanAlreadyExistsException e) {
                System.out.println("Error: " + e.getMessage());
    
                try {
                    Plan existent = planService.getPlanByName(plan.getName());
                    System.out.println("Existent Plan: " + existent);
                    plan.setName(introduceString("Plan", "name"));
                } catch (PlanNotFoundException e1) { // Esto no debería ocurrir
                    System.out.println("Unexpected error: " + e1.getMessage());
                }
            } catch (PlanInvalidInheritanceException e1) {
                System.out.println("Error: " + e1.getMessage());
                return;
            }
        }
    }  

    /* CREATE PLAN FROM PAQUETE FUNCTIONS */

    private static Plan setPlanFromPaqueteFieldsByUser (Plan plan) {
        if (plan.getName() == null) plan.setName(introduceString("Plan", "name"));
        if (plan.getDescription() == null) plan.setDescription(introduceString("Plan", "description"));
        
        System.out.println("Start date: " + plan.getStartDate());
        System.out.println("End date: " + plan.getEndDate());
        
        printList("destination/s", plan.getDestination());
        introduceExtraList("destination/s", plan::addDestination);

        printList("accommodation/s", plan.getAccommodation());
        introduceExtraList("accommodation/s", plan::deleteDestination);

        printList("transportation/s", plan.getTransportation());
        introduceExtraList("transportation/s", plan::addTransportation);
        
        printList("activity/ies", plan.getActivities());
        introduceExtraList("activity/ies", plan::addActivity);

        System.out.println("Price: " + plan.getPrice());

        return plan;
    }

    private static void createPlanFromPaquete() {
        boolean vName, vDescription;
        boolean valid;
        
        Paquete basePaquete = chooseFromList(paqueteService.getAllPaquetes(), "Paquete", Paquete::getName);
        if (basePaquete == null) return;

        Plan plan = new Plan();
        plan = planService.applyBasePaquete(plan, basePaquete);

        do {
            plan = setPlanFromPaqueteFieldsByUser (plan);

            // Confirmate
            String option = askSave("Plan");
            if (option.equals("2")) return;

            // Validate (The only data entered is by the user. The rest are additions to existing lists, so they will always be valid).
            vName = validateMandatoryString(plan.getName());
            vDescription = validateMandatoryString(plan.getDescription());

            valid = vName && vDescription;

            if (!valid) {
                System.out.println("Some mandatory fields are invalid. Please correct them:");
                if (!vName) plan.setName(null);
                if (!vDescription) plan.setDescription(null);
            }
        } while (!valid);

        savePlan(plan);
    }


    /* CREATE PLAN FROM PLANTILLA FUNCTIONS */

    private static void deleteList(String fieldName, Consumer<String> deleteFunction) {
        System.out.print("Delete from Plan " + fieldName + " (comma-separated) or press Enter to skip: ");
        String list = scanner.nextLine().trim();

        if (!list.isEmpty()) {
            Set<String> uniqueItems = Arrays.stream(list.split("\\s*,\\s*"))
                                            .map(String::trim)
                                            .filter(s -> !s.isEmpty())
                                            .collect(Collectors.toCollection(LinkedHashSet::new));
            
            uniqueItems.forEach(deleteFunction);
        }
    }
    
    
    private static void createPlanFromPlantillaOptions(String fieldName, Supplier<List<String>> getListFunction, Consumer<String> addFunction, Consumer<String> deleteFunction) {
        while (true) {
            printList(fieldName, getListFunction.get());
            System.out.println("Do you want to:");
            System.out.println("  1) Add a " + fieldName);
            System.out.println("  2) Eliminate a " + fieldName);
            System.out.println("  3) Keep the actual " + fieldName + " list");
            System.out.print("Option: ");
            String option = scanner.nextLine();
            
            switch (option) {
                case "1" -> introduceExtraList(fieldName, addFunction);
                case "2" -> deleteList(fieldName, deleteFunction);
                case "3" -> {return;}
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static Plan setPlanFromPlantillaFieldsByUser (Plan plan) {
        if (plan.getName() == null) plan.setName(introduceString("Plan", "name"));
        if (plan.getDescription() == null) plan.setDescription(introduceString("Plan", "description"));
        createPlanFromPlantillaOptions("destination/s", plan::getDestination, plan::addDestination, plan::deleteDestination);
        createPlanFromPlantillaOptions("accommodation/s", plan::getAccommodation, plan::addAccommodation, plan::deleteAccommodation);
        createPlanFromPlantillaOptions("transportation/s", plan::getTransportation, plan::addTransportation, plan::deleteTransportation);
        createPlanFromPlantillaOptions("activity/ies", plan::getActivities, plan::addActivity, plan::deleteActivity);
        if (plan.getStartDate() == null) plan.setStartDate(introduceDate("Plan", "start date", ""));
        if (plan.getEndDate() == null) plan.setEndDate(introduceDate("Plan", "end date", " (must be after start date)"));
        if (plan.getPrice() == -1) plan.setPrice(introduceNumber("Plan", "price"));
        return plan;
    } 

    private static void createPlanFromPlantilla() {
        boolean vName, vDescription, vDestination, vAccommodation, vTransportation, vActivities, vStartDate, vEndDate, vPrice;
        boolean valid;
        
        Plantilla basePlantilla = chooseFromList(plantillaService.getAllPlantillas(), "Plantilla", Plantilla::getName);
        if (basePlantilla == null) return;

        Plan plan = new Plan();
        plan = planService.applyBasePlantilla(plan, basePlantilla);

        do {
            plan = setPlanFromPlantillaFieldsByUser (plan);

            // Confirmate
            String option = askSave("Plan");
            if (option.equals("2")) return;

            // Validate
            vName = validateMandatoryString(plan.getName());
            vDescription = validateMandatoryString(plan.getDescription());
            vDestination = validateMandatoryList(plan.getDestination());
            vAccommodation = validateMandatoryList(plan.getAccommodation());
            vTransportation = validateMandatoryList(plan.getTransportation());
            vActivities = validateMandatoryList(plan.getActivities());
            vStartDate = validateMandatoryDate(plan.getStartDate());
            vEndDate = validateEndDate(plan.getEndDate(), plan.getStartDate(), vStartDate);
            vPrice = validateMandatoryPositiveNumber(plan.getPrice());

            valid = vName && vDescription && vDestination && vAccommodation && vTransportation &&
                    vActivities && vStartDate && vEndDate && vPrice;

            if (!valid) {
                System.out.println("Some mandatory fields are invalid. Please correct them:");
                if (!vName) plan.setName(null);
                if (!vDescription) plan.setDescription(null);
                // If the lists are invalid, it is because they were deleted all the items, so null is valid.
                if (!vDestination) plan.setDestination(null);
                if (!vAccommodation) plan.setAccommodation(null);
                if (!vTransportation) plan.setTransportation(null);
                if (!vActivities) plan.setActivities(null);
                if (!vStartDate) plan.setStartDate(null);
                if (!vEndDate) plan.setEndDate(null);
                if (!vPrice) plan.setPrice(-1);
            }
        } while (!valid);

        savePlan(plan);
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
        System.out.println("Exiting... shutting down resources");
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true"); // Cierra Derby embebido explícitamente al salir.
        } catch (SQLException e) {
            // Derby lanza una excepción "esperada" al cerrarse correctamente
            if (!"XJ015".equals(e.getSQLState())) {
                e.printStackTrace(); // si es otra excepción, sí es un problema
            }
        }
        shutdown();
        System.out.println("Shutdown complete. Goodbye!");
    }
}
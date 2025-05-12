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

    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String BLANCO = "\u001B[01m";

    private static void init() {
        try (EntityManager em = AppEntityManagerFactory.getInstance().createEntityManager()) {
            // Initialize the database if necessary
        }
    }

    private static void shutdown() {
        AppEntityManagerFactory.close();
        scanner.close();
    }

    private static Command getCommand() {
        System.out.println(YELLOW + "Choose an option:" + RESET);
        System.out.println(BLANCO + "  1) Create Paquete" + RESET);
        System.out.println(BLANCO + "  2) Create Plantilla" + RESET);
        System.out.println(BLANCO + "  3) Create Plan Específico" + RESET);
        System.out.println(RED + "  q) Exit" + RESET);

        while (true) {
            System.out.print(YELLOW + "Option: " + RESET);
            switch (scanner.nextLine()) {
                case "1": return Command.CREATE_PAQUETE;
                case "2": return Command.CREATE_PLANTILLA;
                case "3": return Command.CREATE_PLAN;
                case "q": return Command.EXIT;
                default: System.out.println(RED + "Invalid option. Try again." + RESET);
            }
        }
    }


    /* DATA FUNCTIONS */
    /**
     * Asks the user whether they want to save a given object.
     *
     * @param className the name of the class or object to be saved (e.g., "Plan", "Package")
     * @return the user's input as a string, typically "1" for Yes or "2" for No
     */
    private static String askSave(String className) {
        System.out.print(YELLOW + "Do you want to save the " + className + ":\n  " +
            GREEN + "1) Yes\n  " +
            RED + "2) No\n" +
            YELLOW + "Option: " + RESET
        );

        return scanner.nextLine();
    }

    /**
     * Prompts the user to input a single-line string for a given class and field.
     *
     * @param className the name of the class (e.g., "Plan", "Package")
     * @param fieldName the name of the field to be entered
     * @return the trimmed input string
     */
    private static String introduceString (String className, String fieldName) {
        System.out.print(BLUE + "Enter " + className + " " + fieldName + ": " + RESET);
        return scanner.nextLine().trim();
    }
    
    /**
     * Validates that a string is not empty.
     *
     * @param input the string to validate
     * @return true if the input is not empty; false otherwise
     */
    private static boolean validateMandatoryString (String input) {
        return !input.isEmpty();
    }


    /**
     * Checks whether a given string is valid by ensuring it is not empty.
     * This method trims the input beforehand if necessary and returns false
     * if the string has no visible content.
     *
     * @param input the input string to validate
     * @return {@code true} if the input is not null and not empty; {@code false} otherwise
     */
    private static boolean validateStartDateAfterToday(LocalDate startDate) {
        return startDate != null && !startDate.isBefore(LocalDate.now());
    }


    /**
     * Prompts the user to enter a comma-separated list of values for a given class and field.
     * Removes duplicates and empty entries, preserving insertion order.
     *
     * @param className the name of the class
     * @param fieldName the name of the field
     * @return a list of unique, trimmed values, or null if input is empty or only contained empty entries
     */
    private static List<String> introduceList (String className, String fieldName) {
        System.out.print(BLUE + "Enter " + className + " " + fieldName + " (comma-separated): " + RESET);
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

    /**
    * Validates that a list is not null.
    *
    * @param list the list to validate
    * @return true if the list is not null; false otherwise
    */
    private static boolean validateMandatoryList (List<String> list) {
        return list != null;
    }

    /**
     * Prompts the user to input a date in yyyy-mm-dd format.
     *
     * @param className the name of the class
     * @param fieldName the name of the field
     * @param extra additional text to display in the prompt
     * @return the parsed LocalDate or null if the input was invalid
     */
    private static LocalDate introduceDate (String className, String fieldName, String extra) {
        LocalDate date;

        System.out.print(BLUE + "Enter " + className + " " + fieldName + " (yyyy-mm-dd)" + extra + ": " + RESET);
        String input = scanner.nextLine().trim();

        try {
            date = LocalDate.parse(input);
        } catch (DateTimeParseException e) {
            return null;
        }

        return date;
    }

    /**
     * Validates that a LocalDate is not null.
     *
     * @param date the date to validate
     * @return true if the date is not null; false otherwise
     */
    private static boolean validateMandatoryDate (LocalDate date) {
        return date != null;
    }

    /**
     * Validates that the end date is valid and, if the start date is also valid, that it comes after the start date.
     *
     * @param endDate the end date to validate
     * @param startDate the start date to compare against
     * @param startDateValidated whether the start date was previously validated
     * @return true if the end date is valid and (if applicable) after the start date
     */
    private static boolean validateEndDate (LocalDate endDate, LocalDate startDate, boolean startDateValidated) {
        if (!validateMandatoryDate (endDate)) return false;
        return (!startDateValidated || endDate.isAfter(startDate)); // If startDate is not correct, true is returned, if not, it is checked that it is later.
    }

    /**
     * Prompts the user to input a number for a given class and field.
     *
     * @param className the name of the class
     * @param fieldName the name of the field
     * @return the parsed number, or -1 if the input was not a valid number
     */
    private static double introduceNumber (String className, String fieldName) {
        double n;

        System.out.print(BLUE + "Enter " + className + " " + fieldName + ": " + RESET);
        String input = scanner.nextLine().trim();
        try {
            n = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return -1;
        }

        return n;
    }

    /**
     * Validates that a number is positive.
     *
     * @param n the number to validate
     * @return true if the number is greater than 0; false otherwise
     */
    private static boolean validateMandatoryPositiveNumber (double n) {
        return n > 0;
    }

    /* PAQUETE FUNCTIONS */

    /**
     * Fills in the missing fields of a Paquete object by prompting the user.
     * Only initially missing fields (null or invalid like -1 for price) are requested.
     *
     * @param paquete the Paquete object to be completed
     * @return the updated Paquete with all required fields filled in by the user
     */
    private static Paquete setPaqueteFieldsByUser (Paquete paquete) {
        if (paquete.getName() == null) paquete.setName(introduceString("Paquete", "name"));
        if (paquete.getDescription() == null) paquete.setDescription(introduceString("Paquete", "description"));
        if (paquete.getDestination() == null) paquete.setDestination(introduceList("Paquete", "destination/s"));
        if (paquete.getAccommodation() == null) paquete.setAccommodation(introduceList("Paquete", "accommodation/s"));
        if (paquete.getTransportation() == null) paquete.setTransportation(introduceList("Paquete", "transportation/s"));
        if (paquete.getActivities() == null) paquete.setActivities(introduceList("Paquete", "activity/ies"));
        if (paquete.getStartDate() == null) paquete.setStartDate(introduceDate("Paquete", "start date", ""));
        if (paquete.getEndDate() == null) paquete.setEndDate(introduceDate("Paquete", "end date", " (must be after start date)"));
        if (paquete.getRequiredPeople() == -1) paquete.setRequiredPeople((int) introduceNumber("Paquete", "required people"));
        if (paquete.getPrice() == -1) paquete.setPrice(introduceNumber("Paquete", "price"));
        return paquete;
    }


    /**
     * Attempts to save a Paquete.
     * If a Paquete with the same name already exists, prompts the user to enter a new name
     * and retries until the Paquete is successfully saved.
     *
     * @param paquete the Paquete to be saved
     */
    private static void savePaquete(Paquete paquete) {
        while (true) {
            try {
                paqueteService.createPaquete(paquete);
                System.out.println(GREEN + "Paquete created successfully." + RESET);
                System.out.println("Details: " + paquete.toString());
                break;
            } catch (PaqueteAlreadyExistsException e) {
                System.out.println(RED + "Error: " + e.getMessage() + RESET);
                
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

    /**
     * Main function in the creation of a Paquete.
     * Prompts for each field, validates required data, and repeats the process
     * until all mandatory fields are valid. Once validated, attempts to save the Paquete.
     */
    private static void createPaquete() {
        boolean vName, vDescription, vDestination, vAccommodation, vTransportation, vActivities, vStartDate, vEndDate, vPrice, vRequiredPeople, valid;
        Paquete paquete = new Paquete();

        do {
            paquete = setPaqueteFieldsByUser (paquete);

            // Confirmate
            String option = askSave("Paquete");
            if (!option.equals("1")) return;

            // Validate
            vName = validateMandatoryString(paquete.getName());
            vDescription = validateMandatoryString(paquete.getDescription());
            vDestination = validateMandatoryList(paquete.getDestination());
            vAccommodation = validateMandatoryList(paquete.getAccommodation());
            vTransportation = validateMandatoryList(paquete.getTransportation());
            vActivities = validateMandatoryList(paquete.getActivities());
            vStartDate = validateMandatoryDate(paquete.getStartDate()) && validateStartDateAfterToday(paquete.getStartDate());
            vEndDate = validateEndDate(paquete.getEndDate(), paquete.getStartDate(), vStartDate);
            vPrice = validateMandatoryPositiveNumber(paquete.getPrice());
            vRequiredPeople = validateMandatoryPositiveNumber(paquete.getRequiredPeople());

            valid = vName && vDescription && vDestination && vAccommodation && vTransportation &&
                    vActivities && vStartDate && vEndDate && vPrice && vRequiredPeople;

            if (!valid) {
                System.out.println(RED + "Some mandatory fields are invalid. Please correct them:" + RESET);
                if (!vName) paquete.setName(null);
                if (!vDescription) paquete.setDescription(null);
                if (!vDestination) paquete.setDestination(null);
                if (!vAccommodation) paquete.setAccommodation(null);
                if (!vTransportation) paquete.setTransportation(null);
                if (!vActivities) paquete.setActivities(null);
                if (!vStartDate) paquete.setStartDate(null);
                if (!vEndDate) paquete.setEndDate(null);
                if (!vPrice) paquete.setPrice(-1);
                if (!vRequiredPeople) paquete.setRequiredPeople(-1);
            }
        } while (!valid);

        savePaquete(paquete);
    }

    /* PLANTILLA FUNCTIONS */

    /**
     * Fills in the missing fields of a Plantilla object by prompting the user.
     * Only initially missing fields (null) are requested.
     *
     * @param plantilla the Plantilla object to be completed
     * @return the updated Plantilla with all required fields filled in by the user
     */
    private static Plantilla setPlantillaFieldsByUser (Plantilla plantilla) {
        if (plantilla.getName() == null) plantilla.setName(introduceString("Plantilla", "name"));
        if (plantilla.getDescription() == null) plantilla.setDescription(introduceString("Plantilla", "description"));
        if (plantilla.getDestination() == null) plantilla.setDestination(introduceString("Plantilla", "destination/s"));
        if (plantilla.getAccommodation() == null) plantilla.setAccommodation(introduceString("Plantilla", "accommodation/s"));
        if (plantilla.getTransportation() == null) plantilla.setTransportation(introduceList("Plantilla", "transportation/s"));
        if (plantilla.getActivities() == null) plantilla.setActivities(introduceList("Plantilla", "activity/ies"));
        return plantilla;
    }

    /**
     * Attempts to save a Plantilla.
     * If a Plantilla with the same name already exists, prompts the user to enter a new name
     * and retries until the Plantilla is successfully saved.
     *
     * @param plantilla the Plantilla to be saved
     */
    private static void savePlantilla(Plantilla plantilla) {
        while (true) {
            try {
                plantillaService.createPlantilla(plantilla);
                System.out.println(GREEN + "Plantilla created successfully." + RESET);
                System.out.println("Details: " + plantilla.toString());
                break;
            } catch (PlantillaAlreadyExistsException e) {
                System.out.println(RED + "Error: " + e.getMessage() + RESET);
    
                try {
                    Plantilla existent = plantillaService.getPlantillaByName(plantilla.getName());
                    System.out.println("Existent Plantilla: " + existent);
                    plantilla.setName(introduceString("Plantilla", "name"));
                } catch (PlantillaNotFoundException e1) { // Esto no debería ocurrir
                    System.out.println(RED + "Unexpected error: " + e1.getMessage() + RESET);
                }
            }
        }
    }    

    /**
     * Main function in the creation of a Plantilla.
     * Prompts for each field, validates required data, and repeats the process
     * until all mandatory fields are valid. Once validated, attempts to save the Plantilla.
     */
    private static void createPlantilla() {
        boolean vName, vDescription, vDestination, vAccommodation, vTransportation, vActivities;
        boolean valid;
        Plantilla plantilla = new Plantilla();

        do {
            plantilla = setPlantillaFieldsByUser (plantilla);

            // Confirmate
            String option = askSave("Plantilla");
            if (!option.equals("1")) return;

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
                System.out.println(RED + "Some mandatory fields are invalid. Please correct them:" + RESET);
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

    /**
     * Prompts the user to choose how to create a Plan: from a Plantilla or a Paquete.
     * Calls the corresponding method based on the user's input.
     */
    private static void createPlan() {
        System.out.println(YELLOW + "Do you want to base the Plan on:" + RESET);
        System.out.println(BLANCO + "  1) A Plantilla" + RESET);
        System.out.println(BLANCO + "  2) A Paquete" + RESET);
        System.out.print(YELLOW + "Option: " + RESET);
        String option = scanner.nextLine();

        switch (option) {
            case "1" -> createPlanFromPlantilla();
            case "2" -> createPlanFromPaquete();
            default -> System.out.println(RED + "Invalid option." + RESET);
        }
    }

    /**
     * Displays a numbered list of Paquetes or Plantillas and prompts the user to choose one by number.
     * If the list is empty or the input is invalid, returns null.
     *
     * @param items the list of Paquetes or Plantillas to choose from
     * @param className the display name of the item type ("Plan" or "Paquete")
     * @param nameExtractor class of the items of the list (Plan or Paquete)
     * @return the selected item, or null if the selection is invalid
     */
    private static <T> T chooseFromList (List<T> items, String className, Function<T, String> nameExtractor) {
        if (items.isEmpty()) {
            System.out.println(RED + "No " + className + "s available to base the Plan on." + RESET);
            return null;
        }
    
        System.out.println(BLUE + "Available " + className + "s:" + RESET);
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ") " + nameExtractor.apply(items.get(i)));
        }
    
        System.out.print(YELLOW + "Choose a " + className + ": " + RESET);
        try {
            int choice = Integer.parseInt(scanner.nextLine()) - 1;
            if (choice < 0 || choice >= items.size()) {
                System.out.println(RED + "Invalid choice." + RESET);
                return null;
            }
            return items.get(choice);
        } catch (NumberFormatException e) {
            System.out.println(RED + "Invalid input. Must be a number." + RESET);
            return null;
        }
    }

    /**
     * Prints the current contents of a list field.
     * If the list is null, it prints "none".
     *
     * @param fieldName the name of the field being printed (e.g., "destination/s")
     * @param list      the list of values to display
     */
    private static void printList (String fieldName, List<String> list) {
        System.out.println(BLUE + "Current " + fieldName + ": " + (list != null ? String.join(", ", list) : "none") + RESET); // Validation just in case, although there should always be at least one.
    }

    /**
     * Prompts the user to enter additional values for a list field, comma-separated.
     * Skips if the input is empty. Adds each unique, trimmed value using the provided add function.
     *
     * @param fieldName   the name of the field (e.g., "destination/s")
     * @param addFunction a Consumer that adds a single value to the list
     */
    private static void introduceExtraList(String fieldName, Consumer<String> addFunction) {
        System.out.print(BLUE + "Add Plan extra " + fieldName + " (comma-separated) or press Enter to skip: " + RESET);
        String list = scanner.nextLine().trim();

        if (!list.isEmpty()) {
            Set<String> uniqueItems = Arrays.stream(list.split("\\s*,\\s*"))
                                            .map(String::trim)
                                            .filter(s -> !s.isEmpty())
                                            .collect(Collectors.toCollection(LinkedHashSet::new));
            
            uniqueItems.forEach(addFunction);
        }
    }
    
    /**
     * Attempts to save a Plan.
     * If a Plan with the same name already exists, prompts the user to enter a new name
     * and retries until the Plan is successfully saved.
     *
     * @param plan the Plan to be saved
     */
    private static void savePlan(Plan plan) {
        while (true) {
            try {
                planService.createPlan(plan);
                System.out.println(GREEN + "Plan created successfully." + RESET);
                System.out.println("Details: " + plan.toString());
                break;
            } catch (PlanAlreadyExistsException e) {
                System.out.println(RED + "Error: " + e.getMessage() + RESET);
    
                try {
                    Plan existent = planService.getPlanByName(plan.getName());
                    System.out.println("Existent Plan: " + existent);
                    plan.setName(introduceString("Plan", "name"));
                } catch (PlanNotFoundException e1) { // Esto no debería ocurrir
                    System.out.println(RED + "Unexpected error: " + e1.getMessage() + RESET);
                }
            } catch (PlanInvalidInheritanceException e1) {
                System.out.println(RED + "Error: " + e1.getMessage() + RESET);
                return;
            }
        }
    }  

    /* CREATE PLAN FROM PAQUETE FUNCTIONS */
    /**
     * Complements fields of a Plan created after a Paquete object by prompting the user.
     * Only list fields are requested to add extra information.
     *
     * @param plan the Plan object to be completed
     * @return the updated Plan with all extra data introduced by the user
     */
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

    /**
     * Main function in the creation of a Plan from Paquete.
     * Prompts for each field, validates required data, and repeats the process
     * until all mandatory fields are valid. Once validated, attempts to save the Plan.
     */
    private static void createPlanFromPaquete() {
        boolean vName = true, vDescription = true;
        boolean valid;
        boolean firstIteration = true;

        Paquete basePaquete = chooseFromList(paqueteService.getAllPaquetes(), "Paquete", Paquete::getName);
        if (basePaquete == null) return;

        Plan plan = new Plan();
        plan = planService.applyBasePaquete(plan, basePaquete);

        do {
            // Mostrar y pedir todos los campos la primera vez
            if (firstIteration || !vName) plan.setName(introduceString("Plan", "name"));
            if (firstIteration || !vDescription) plan.setDescription(introduceString("Plan", "description"));

            if (firstIteration) {
                System.out.println("Start date: " + plan.getStartDate());
                System.out.println("End date: " + plan.getEndDate());
                System.out.println("Price: " + plan.getPrice());

                printList("destination/s", plan.getDestination());
                introduceExtraList("destination/s", plan::addDestination);

                printList("accommodation/s", plan.getAccommodation());
                introduceExtraList("accommodation/s", plan::addAccommodation);

                printList("transportation/s", plan.getTransportation());
                introduceExtraList("transportation/s", plan::addTransportation);

                printList("activity/ies", plan.getActivities());
                introduceExtraList("activity/ies", plan::addActivity);
            }

            firstIteration = false;

            String option = askSave("Plan");
            if (!option.equals("1")) return;

            vName = validateMandatoryString(plan.getName());
            vDescription = validateMandatoryString(plan.getDescription());

            valid = vName && vDescription;

            if (!valid) {
                System.out.println(RED + "Some mandatory fields are invalid. Please correct them:" + RESET);
                if (!vName) plan.setName(null);
                if (!vDescription) plan.setDescription(null);
            }

        } while (!valid);

        savePlan(plan);
    }


    /* CREATE PLAN FROM PLANTILLA FUNCTIONS */

    /**
     * Prompts the user to enter additional values to eliminate from a list field, comma-separated.
     * Skips if the input is empty. Eliminates each unique, trimmed value using the provided add function.
     *
     * @param fieldName   the name of the field (e.g., "destination/s")
     * @param deleteFunction a Consumer that eliminates a single value to the list
     */
    private static void deleteList(String fieldName, Consumer<String> deleteFunction) {
        System.out.print(RED + "Delete from Plan " + fieldName + " (comma-separated) or press Enter to skip: " + RESET);
        String list = scanner.nextLine().trim();

        if (!list.isEmpty()) {
            Set<String> uniqueItems = Arrays.stream(list.split("\\s*,\\s*"))
                                            .map(String::trim)
                                            .filter(s -> !s.isEmpty())
                                            .collect(Collectors.toCollection(LinkedHashSet::new));
            
            uniqueItems.forEach(deleteFunction);
        }
    }
    
    /**
     * Allows the user to view, add to, or remove items from a list field interactively.
     * Continues prompting until the user chooses to keep the current list as-is.
     *
     * @param fieldName      the name of the field (used for display purposes)
     * @param getListFunction a Supplier that returns the current list
     * @param addFunction     a Consumer that adds a new item to the list
     * @param deleteFunction  a Consumer that removes an item from the list
     */
    private static void createPlanFromPlantillaOptions(String fieldName, Supplier<List<String>> getListFunction, Consumer<String> addFunction, Consumer<String> deleteFunction) {
        while (true) {
            if (getListFunction == null) {
                printList(fieldName, null);
                introduceExtraList(fieldName, addFunction);
            }
            else {
                printList(fieldName, getListFunction.get());
                System.out.println(YELLOW + "Do you want to:" + RESET);
                System.out.println(GREEN + "  1) Add a " + fieldName + RESET);
                System.out.println(RED + "  2) Eliminate a " + fieldName + RESET);
                System.out.println(BLUE + "  3) Keep the actual " + fieldName + " list" + RESET);
                System.out.print(YELLOW + "Option: " + RESET);
                String option = scanner.nextLine();
                
                switch (option) {
                    case "1" -> introduceExtraList(fieldName, addFunction);
                    case "2" -> deleteList(fieldName, deleteFunction);
                    case "3" -> {return;}
                    default -> System.out.println(RED + "Invalid option." + RESET);
                }
            }
        }
    }

    
    /**
     * Main function in the creation of a Plan from Plantilla.
     * Prompts for each field, validates required data, and repeats the process
     * until all mandatory fields are valid. Once validated, attempts to save the Plan.
     */
    private static void createPlanFromPlantilla() {
        boolean vName = true, vDescription = true, vDestination = true, vAccommodation = true,
            vTransportation = true, vActivities = true, vStartDate = true, vEndDate = true, vPrice = true;
        boolean valid;
        boolean firstIteration = true;

        Plantilla basePlantilla = chooseFromList(plantillaService.getAllPlantillas(), "Plantilla", Plantilla::getName);
        if (basePlantilla == null) return;

        Plan plan = new Plan();
        plan = planService.applyBasePlantilla(plan, basePlantilla);

        do {
            if (firstIteration) {
                plan.setName(introduceString("Plan", "name"));
                plan.setDescription(introduceString("Plan", "description"));
                createPlanFromPlantillaOptions("destination/s", plan::getDestination, plan::addDestination, plan::deleteDestination);
                createPlanFromPlantillaOptions("accommodation/s", plan::getAccommodation, plan::addAccommodation, plan::deleteAccommodation);
                createPlanFromPlantillaOptions("transportation/s", plan::getTransportation, plan::addTransportation, plan::deleteTransportation);
                createPlanFromPlantillaOptions("activity/ies", plan::getActivities, plan::addActivity, plan::deleteActivity);
                plan.setStartDate(introduceDate("Plan", "start date", ""));
                plan.setEndDate(introduceDate("Plan", "end date", " (must be after start date)"));
                plan.setPrice(introduceNumber("Plan", "price"));
            } else {
                if (!vName) plan.setName(introduceString("Plan", "name"));
                if (!vDescription) plan.setDescription(introduceString("Plan", "description"));
                if (!vDestination) createPlanFromPlantillaOptions("destination/s", plan::getDestination, plan::addDestination, plan::deleteDestination);
                if (!vAccommodation) createPlanFromPlantillaOptions("accommodation/s", plan::getAccommodation, plan::addAccommodation, plan::deleteAccommodation);
                if (!vTransportation) createPlanFromPlantillaOptions("transportation/s", plan::getTransportation, plan::addTransportation, plan::deleteTransportation);
                if (!vActivities) createPlanFromPlantillaOptions("activity/ies", plan::getActivities, plan::addActivity, plan::deleteActivity);
                if (!vStartDate) plan.setStartDate(introduceDate("Plan", "start date", ""));
                if (!vEndDate) plan.setEndDate(introduceDate("Plan", "end date", " (must be after start date)"));
                if (!vPrice) plan.setPrice(introduceNumber("Plan", "price"));
            }

            String option = askSave("Plan");
            if (!option.equals("1")) return;

            vName = validateMandatoryString(plan.getName());
            vDescription = validateMandatoryString(plan.getDescription());
            vDestination = validateMandatoryList(plan.getDestination());
            vAccommodation = validateMandatoryList(plan.getAccommodation());
            vTransportation = validateMandatoryList(plan.getTransportation());
            vActivities = validateMandatoryList(plan.getActivities());
            vStartDate = validateMandatoryDate(plan.getStartDate()) && validateStartDateAfterToday(plan.getStartDate());
            vEndDate = validateEndDate(plan.getEndDate(), plan.getStartDate(), vStartDate);
            vPrice = validateMandatoryPositiveNumber(plan.getPrice());

            valid = vName && vDescription && vDestination && vAccommodation && vTransportation &&
                    vActivities && vStartDate && vEndDate && vPrice;

            if (!valid) {
                System.out.println(RED + "Some mandatory fields are invalid. Please correct them:" + RESET);
            }

            firstIteration = false;

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
        System.out.println(RED + "Exiting... shutting down resources" + RESET);
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true"); // Cierra Derby embebido explícitamente al salir.
        } catch (SQLException e) {
            // Derby lanza una excepción "esperada" al cerrarse correctamente
            if (!"XJ015".equals(e.getSQLState())) {
                e.printStackTrace(); // si es otra excepción, sí es un problema
            }
        }
        shutdown();
        System.out.println(RED + "Shutdown complete. Goodbye!" + RESET);
    }
}
/**
 * Copyright 2022-2025 Information Retrieval Lab
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.irlab;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.irlab.common.AppEntityManagerFactory;
import org.irlab.model.exceptions.RoleNotFoundException;
import org.irlab.model.exceptions.UserAlreadyExistsException;
import org.irlab.model.exceptions.UserNotFoundException;
import org.irlab.model.services.RoleService;
import org.irlab.model.services.RoleServiceImpl;
import org.irlab.model.services.UserService;
import org.irlab.model.services.UserServiceImpl;

import jakarta.persistence.EntityManager;

public class App {

    private enum Command {
        GREET_USER, CHANGE_GREETING, CREATE_USER, EXIT
    }

    private static final int CORRECT_SHUTDOWN = 50000;

    private static UserService userService = null;

    private static RoleService roleService = null;

    private static Scanner scanner = null;

    private static void init() {
        try (EntityManager em = AppEntityManagerFactory.getInstance().createEntityManager()) {}
        try {
            userService = new UserServiceImpl();
        } catch (RoleNotFoundException e) {
            System.out.println(
                    """
                            Could not find the default role in the database.

                            This usually means the database has not been populated. The schema has now been created if
                            it didn't exists already. You can now populate the database with:

                              mvn sql:execute

                            Have a good day!
                            """);
            System.exit(1);
        }
        roleService = new RoleServiceImpl();
    }

    private static void shutdown() throws SQLException {
        AppEntityManagerFactory.close();

        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException e) {
            if (e.getErrorCode() != CORRECT_SHUTDOWN) {
                throw e;
            }
        }
    }

    private static Command getCommand() {
        System.out.println("Choose an option:");
        System.out.println("  1) Greet user");
        System.out.println("  2) Change user greeting");
        System.out.println("  3) Add a new user");
        System.out.println();
        System.out.println("  q) Exit");
        System.out.println();
        while (true) {
            System.out.print("Option: ");
            String input = scanner.nextLine();
            if (input.length() == 0) {
                System.out.println("An option needs to be introduced");
            } else if (input.length() > 1) {
                System.err.println(input + " is not a valid option");
            } else {
                switch (input.charAt(0)) {
                case '1':
                    return Command.GREET_USER;
                case '2':
                    return Command.CHANGE_GREETING;
                case '3':
                    return Command.CREATE_USER;
                case 'q':
                    return Command.EXIT;
                default:
                    System.out.println(input + " is not a valid option");
                }
            }
        }
    }

    private static @Nonnull String readInput(String message, String errorMessage) {
        String result = null;
        while (result == null) {
            System.out.print(message);
            String input = scanner.nextLine();
            if (input.length() == 0) {
                System.out.println(errorMessage);
            } else {
                result = input;
            }
        }
        return result;
    }

    private static void greetUser() {
        String userName = readInput("User name: ", "You must supply a user name");
        String greetingMessage = userService.greet(userName);
        System.out.println(greetingMessage);
    }

    private static void changeGreeting() {
        String userName = readInput("User name: ", "You must supply a user name");
        String newGreeting = readInput("Greeting message: ",
                "You must supply a new greeting message");
        try {
            userService.setUserGreeting(userName, newGreeting);
            System.out.println("User greeting changed");
        } catch (UserNotFoundException e) {
            System.out.println(
                    String.format("Greeting could not be changed, due to the following error:\n%s",
                            e.getMessage()));
        }

    }

    private static @Nonnull String askForRole() {
        String roleList = roleService.getAvailableRoleNames().stream()
                .map(name -> "  - " + name + "\n").collect(Collectors.joining(""));
        String prompt = "Select a role for the user. Available roles are:\n" + roleList
                + "User role: ";
        return readInput(prompt, "You must supply a role name");
    }

    private static void createUser() {
        String userName = readInput("User name: ", "You must supply a user name");
        String roleName = askForRole();
        try {
            userService.createUser(userName, roleName);
            System.out.println("User created");
        } catch (UserAlreadyExistsException e) {
            System.out.println("User not created: a user with that name already exists.");
        } catch (RoleNotFoundException e) {
            System.out.println("User not created: invalid role");
        }
    }

    public static void main(String[] args) throws SQLException {
        init();
        boolean exit = false;
        scanner = new Scanner(System.in);
        while (!exit) {
            System.out.println();
            Command command = getCommand();
            switch (command) {
            case GREET_USER -> greetUser();
            case CHANGE_GREETING -> changeGreeting();
            case CREATE_USER -> createUser();
            case EXIT -> exit = true;
            }
        }

        shutdown();
    }
}

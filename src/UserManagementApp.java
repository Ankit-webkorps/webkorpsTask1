import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UserManagementApp {
    private static final UserManager userManager = new UserManager();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                handleChoice(choice);
                running = choice != 6;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number 1-6.");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== User Management System ===");
        System.out.println("1. Create User");
        System.out.println("2. View All Users");
        System.out.println("3. View Single User");
        System.out.println("4. Update User");
        System.out.println("5. Delete User");
        System.out.println("6. Exit");
        System.out.print("Enter your choice (1-6): ");
    }

    private static void handleChoice(int choice) throws UserNotFoundException {
        switch (choice) {
            case 1 -> createUser();
            case 2 -> viewAllUsers();
            case 3 -> viewSingleUser();
            case 4 -> updateUser();
            case 5 -> deleteUser();
            case 6 -> System.out.println("Exiting system...");
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void createUser() {
        System.out.println("\n--- Create New User ---");

        String name = getInputWithRetry(
                "Enter user name" ,
                ValidationUtil::isValidName,
                "Invalid name. Use only letters and spaces (2-50 chars)."
        );
        if (name == null) {
            System.out.println("User creation cancelled.");
            return;
        }

        String email = getInputWithRetry(
                "Enter email",
                ValidationUtil::isValidEmail,
                "Invalid email format."
        );
        if (email == null) {
            System.out.println("User creation cancelled.");
            return;
        }

        if (userManager.getAllUsers().stream().anyMatch(u -> u.getEmail().equals(email))) {
            System.out.println("Error: Email already exists.");
            System.out.println("Would you like to:");
            int choice = ValidationUtil.getChoiceOnInvalidInput();
            if (choice == 1) {
                System.out.println("User creation cancelled.");
                return;
            }
            else {
                createUser(); // Restart the process
                return;
            }
        }

        List<String> numbers = getPhoneNumbersWithRetry();
        if (numbers == null) {
            System.out.println("User creation cancelled.");
            return;
        }

        userManager.createUser(name, email, numbers);
        System.out.println("User created successfully!");
    }

    private static String getInputWithRetry(String prompt, InputValidator validator, String errorMsg) {
        while (true) {
            System.out.print(prompt + " (or '0' to return to main menu): ");
            String input = scanner.nextLine().trim();

            if (input.equals("0")) {
                return null;
            }

            if (validator.validate(input)) {
                return input;
            }

            System.out.println(errorMsg);
            System.out.println("Would you like to:");
            int choice = ValidationUtil.getChoiceOnInvalidInput();

            if (choice == 1) {
                return null;
            }
        }
    }

    private static List<String> getPhoneNumbersWithRetry() {
        while (true) {
            System.out.println("Enter phone numbers (comma separated) (or '0' to return to main menu):");
            String input = scanner.nextLine().trim();

            if (input.equals("0")) {
                return null;
            }

            List<String> numbers = new ArrayList<>();
            boolean allValid = true;

            String[] entries = input.split(",");
            for (String entry : entries) {
                String number = entry.trim();
                if (!number.isEmpty()) {
                    if (ValidationUtil.isValidMobileNumber(number)) {
                        numbers.add(ValidationUtil.formatPhoneNumber(number));
                    } else {
                        System.out.println("Invalid number: " + number);
                        allValid = false;
                        break;
                    }
                }
            }

            if (allValid && !numbers.isEmpty()) {
                return numbers;
            }

            System.out.println("Invalid phone number(s). Would you like to:");
            int choice = ValidationUtil.getChoiceOnInvalidInput();
            if (choice == 1) {
                return null;
            }
        }
    }

    private static void viewAllUsers() {
        List<User> users = userManager.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            System.out.println("\n--- All Users ---");
            users.forEach(System.out::println);
        }
    }

    private static void viewSingleUser() throws UserNotFoundException {
        while (true) {
            System.out.print("\nEnter user email to search (or '0' to return to main menu): ");
            String email = scanner.nextLine().trim();

            if (email.equals("0")) {
                System.out.println("Returning to main menu...");
                return;
            }

            if (!ValidationUtil.isValidEmail(email)) {
                System.out.println("Error: Invalid email format. Please enter a valid email address.");
                System.out.println("Example of valid email: user@example.com");

                System.out.println("\nWould you like to:");
                System.out.println("1. Try again with a different email");
                System.out.println("2. Cancel the operation");
                System.out.print("Enter your choice (1-2): ");

                int choice = getValidChoice(1, 2);
                if (choice == 2) {
                    System.out.println("Operation cancelled.");
                    return;
                }
                continue;
            }

            try {
                User user = userManager.getUser(email);
                System.out.println("\nUser found:");
                System.out.println(user);
                return;
            } catch (UserNotFoundException e) {
                System.out.println("Error: User not found with email: " + email);

                System.out.println("\nWould you like to:");
                System.out.println("1. Try again with a different email");
                System.out.println("2. Cancel the operation");
                System.out.print("Enter your choice (1-2): ");

                int choice = getValidChoice(1, 2);
                if (choice == 2) {
                    System.out.println("Operation cancelled.");
                    return;
                }
            }
        }
    }

    private static int getValidChoice(int min, int max) {
        while (true) {
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                if (choice >= min && choice <= max) {
                    return choice;
                }
                System.out.print("Please enter a number between " + min + " and " + max + ": ");
            } catch (InputMismatchException e) {
                System.out.print("Please enter a valid number: ");
                scanner.nextLine(); // clear invalid input
            }
        }
    }

    private static void updateUser() throws UserNotFoundException {
        System.out.print("\nEnter user email to update (or '0' to return to main menu): ");
        String email = scanner.nextLine().trim();
        if (email.equals("0")) {
            System.out.println("Returning to main menu...");
            return;
        }

        User user = userManager.getUser(email);
        System.out.println("\nCurrent details:\n" + user);

        while (true) {
            System.out.println("\nWhat would you like to update?");
            System.out.println("1. Name");
            System.out.println("2. Phone numbers");
            System.out.println("3. Both name and phone numbers");
            System.out.println("4. Cancel");
            System.out.print("Enter choice (1-4) or '0' to return to main menu: ");

            String choiceStr = scanner.nextLine();
            if (choiceStr.equals("0")) {
                System.out.println("Returning to main menu...");
                return;
            }

            try {
                int choice = Integer.parseInt(choiceStr);

                switch (choice) {
                    case 1 -> {
                        String name = getInputWithRetry("Enter new name", ValidationUtil::isValidName,
                                "Invalid name format.");
                        if (name != null) {
                            userManager.updateUser(email, name, user.getMobileNumbers());
                            System.out.println("Name updated successfully!");
                        } else {
                            System.out.println("Update cancelled.");
                        }
                        return;
                    }
                    case 2 -> {
                        List<String> numbers = getPhoneNumbersWithRetry();
                        if (numbers != null) {
                            userManager.updateUser(email, user.getName(), numbers);
                            System.out.println("Phone numbers updated successfully!");
                        } else {
                            System.out.println("Update cancelled.");
                        }
                        return;
                    }
                    case 3 -> {
                        String name = getInputWithRetry("Enter new name", ValidationUtil::isValidName,
                                "Invalid name format.");
                        if (name == null) {
                            System.out.println("Update cancelled.");
                            break;
                        }

                        List<String> numbers = getPhoneNumbersWithRetry();
                        if (numbers != null) {
                            userManager.updateUser(email, name, numbers);
                            System.out.println("User details updated successfully!");
                        } else {
                            System.out.println("Update cancelled.");
                        }
                        return;
                    }
                    case 4 -> {
                        System.out.println("Update cancelled.");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please enter 1-4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number 1-4 or '0'.");
            }
        }
    }

    private static void deleteUser() {
        System.out.print("\nEnter user email to delete (or '0' to return to main menu): ");
        String email = scanner.nextLine().trim();
        if (email.equals("0")) {
            System.out.println("Returning to main menu...");
            return;
        }

        if (userManager.deleteUser(email)) {
            System.out.println("User deleted successfully.");
        } else {
            System.out.println("User not found.");
        }
    }

    @FunctionalInterface
    private interface InputValidator {
        boolean validate(String input);
    }
}

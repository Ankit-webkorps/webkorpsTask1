import java.util.*;

public class UserManagementApp {
     static  UserManager userManager = new UserManager();
     static  Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                handleChoice(choice);
                if(choice==6)
                    running =false;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void printMenu()
    {
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
            case 1:
                createUser();
                break;
            case 2:
                viewAllUsers();
                break;
            case 3:
                viewSingleUser();
                break;
            case 4:
                updateUser();
                break;
            case 5:
                deleteUser();
                break;
            case 6:
                System.out.println("Exiting system...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void createUser() {
        System.out.println("\n--- Create New User ---");

        String name = getInputWithRetry();
        if (name == null) {
            System.out.println("User creation cancelled.");
            return;
        }

        String email = getInputWithRetry1();

        if (email == null) {
            System.out.println("User creation cancelled.");
            return;
        }

        if (userManager.containsemail(email)) {
            System.out.println("Error: Email already exists.");
            System.out.println("Would you like to:");
            int choice = ValidationUtil.getChoiceOnInvalidInput();
            if (choice == 1) {
                System.out.println("User creation cancelled.");
                return;
            } else {
                createUser(); // Restart the process
                return;
            }
        }

        List<String> numbers = getPhoneNumbersWithRetry();
        if (numbers == null)
        {
            System.out.println("User creation cancelled.");
            return;
        }

        userManager.createUser(name, email, numbers);
        System.out.println("User created successfully!");
    }

    private static String getInputWithRetry() {
        while (true) {
            System.out.print("Enter user name" + " (or '0' to return to main menu): ");
            String input = scanner.nextLine().trim();

            if (input.equals("0")) {
                return null;
            }

            if (ValidationUtil.isValidName(input)) {
                return input;
            }

            System.out.println("Invalid name format.");
            System.out.println("Would you like to:");
            int choice = ValidationUtil.getChoiceOnInvalidInput();

            if (choice == 1) {
                return null;
            }
        }
    }


    private static String getInputWithRetry1() {
        while (true) {
            System.out.print("Enter email" + " (or '0' to return to main menu): ");
            String input = scanner.nextLine().trim();

            if (input.equals("0")) {
                return null;
            }

            if (ValidationUtil.isValidEmail(input)) {
                return input;
            }

            System.out.println("invalid email format");
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
                        numbers.add(number);
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
            for(User u:users){
                System.out.println(u);
            }
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


                int choice = ValidationUtil.getChoiceOnInvalidInput();
                if (choice == 2) {
                    System.out.println("Operation cancelled.");
                    return;
                }
                continue;
            }

            try {
                User user = UserManager.users.get(email);
                if (user == null) {
                    throw new UserNotFoundException("User not found with email: " + email);
                }
                System.out.println("\nUser found:");
                System.out.println(user);
                return;
            } catch (UserNotFoundException e) {
                System.out.println("Error: User not found with email: " + email);

                System.out.println("\nWould you like to:");
                System.out.println("1. Try again with a different email");
                System.out.println("2. Cancel the operation");
                System.out.print("Enter your choice (1-2): ");

                int choice = ValidationUtil.getChoiceOnInvalidInput();
                if (choice == 2) {
                    System.out.println("Operation cancelled.");
                    return;
                }
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

        User user =UserManager.users.get(email);
        if (user == null) {
            throw new UserNotFoundException("User not found with email: " + email);
        }
        System.out.println("\nCurrent details:\n" + user);

        while (true) {
            System.out.println("\nWhat would you like to update?");
            System.out.println("1. Name");
            System.out.println("2. Phone numbers");
            System.out.println("3. Both name and phone numbers");
            System.out.println("4. Cancel");
            System.out.print("Enter choice (1-4) or '0' to return to main menu: ");

            String choiceStr = scanner.nextLine();
            if (choiceStr.equals("0"))
            {
                System.out.println("Returning to main menu...");
                return;
            }

            try {
                int choice = Integer.parseInt(choiceStr);

                switch (choice) {
                    case 1 : {
                        String name = getInputWithRetry();
                        if (name != null) {
                            user.setName(name);
                            System.out.println("Name updated successfully!");
                        } else {
                            System.out.println("Update cancelled.");
                        }
                        return;
                    }
                    case 2 : {
                        List<String> numbers = getPhoneNumbersWithRetry();
                        if (numbers != null) {
                            user.setMobileNumbers(numbers);
                            System.out.println("Phone numbers updated successfully!");
                        } else {
                            System.out.println("Update cancelled.");
                        }
                        return;
                    }
                    case 3 : {
                        String name = getInputWithRetry();
                        if (name == null) {
                            System.out.println("Update cancelled.");
                            break;
                        }

                        List<String> numbers = getPhoneNumbersWithRetry();
                        if (numbers != null) {
                            user.setMobileNumbers(numbers);
                            user.setName(name);
                            System.out.println("User details updated successfully!");
                        } else {
                            System.out.println("Update cancelled.");
                        }
                        return;
                    }

                    case 4 : {
                        System.out.println("Update cancelled.");
                        return;
                    }

                    default : System.out.println("Invalid choice. Please enter 1-4.");
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
}

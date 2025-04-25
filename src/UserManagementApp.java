//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.*;
import java.util.regex.*;

public class UserManagementApp {
    private static UserManager userManager = new UserManager();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println("\nUser Management System");
            System.out.println("1. Create User");
            System.out.println("2. View All Users");
            System.out.println("3. View Single User");
            System.out.println("4. Update User");
            System.out.println("5. Delete User");
            System.out.println("6. Add Mobile Number to User");
            System.out.println("7. Remove Mobile Number from User");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

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
                        addMobileNumber();
                        break;
                    case 7:
                        removeMobileNumber();
                        break;
                    case 8:
                        running = false;
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void createUser() {
        System.out.println("\nCreate New User");
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        List<String> mobileNumbers = new ArrayList<>();
        System.out.println("Enter mobile numbers (enter 'done' when finished):");
        while (true) {
            System.out.print("Mobile number: ");
            String number = scanner.nextLine();
            if (number.equalsIgnoreCase("done")) {
                break;
            }
            mobileNumbers.add(number);
        }

        try {
            boolean created = userManager.createUser(name, email, mobileNumbers);
            if (created) {
                System.out.println("User created successfully.");
            } else {
                System.out.println("User with this email already exists.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }
    }

    private static void viewAllUsers() {
        List<User> users = userManager.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            System.out.println("\nAll Users:");
            for (User user : users) {
                System.out.println(user);
            }
        }
    }

    private static void viewSingleUser() {
        System.out.print("\nEnter user email to search: ");
        String email = scanner.nextLine();

        try {
            User user = userManager.getUser(email);
            System.out.println("User found:");
            System.out.println(user);
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void updateUser() {
        System.out.print("\nEnter user email to update: ");
        String email = scanner.nextLine();

        try {
            User user = userManager.getUser(email);
            System.out.println("Current user details:");
            System.out.println(user);

            System.out.print("Enter new name (leave blank to keep current): ");
            String newName = scanner.nextLine();
            if (!newName.isEmpty()) {
                userManager.updateUserName(email, newName);
                System.out.println("Name updated successfully.");
            }

        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void deleteUser() {
        System.out.print("\nEnter user email to delete: ");
        String email = scanner.nextLine();

        boolean deleted = userManager.deleteUser(email);
        if (deleted) {
            System.out.println("User deleted successfully.");
        } else {
            System.out.println("User not found.");
        }
    }

    private static void addMobileNumber() {
        System.out.print("\nEnter user email: ");
        String email = scanner.nextLine();

        System.out.print("Enter mobile number to add: ");
        String number = scanner.nextLine();

        try {
            boolean added = userManager.addUserMobileNumber(email, number);
            if (added) {
                System.out.println("Mobile number added successfully.");
            }
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }
    }

    private static void removeMobileNumber() {
        System.out.print("\nEnter user email: ");
        String email = scanner.nextLine();

        System.out.print("Enter mobile number to remove: ");
        String number = scanner.nextLine();

        try {
            boolean removed = userManager.removeUserMobileNumber(email, number);
            if (removed) {
                System.out.println("Mobile number removed successfully.");
            } else {
                System.out.println("Mobile number not found for this user.");
            }
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
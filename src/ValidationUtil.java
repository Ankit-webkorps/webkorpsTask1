import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.regex.Pattern;

public class ValidationUtil {
    private static final Scanner scanner = new Scanner(System.in);


    public static int getChoiceOnInvalidInput() {
        System.out.println("\n1. Exit this operation");
        System.out.println("2. Try again");
        System.out.print("Enter your choice (1-2): ");

        while (true) {
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                if (choice == 1 || choice == 2) {
                    return choice;
                }
                System.out.print("Please enter 1 or 2: ");
            } catch (InputMismatchException e) {
                System.out.print("Please enter a number (1 or 2): ");
                scanner.nextLine();
            }
        }
    }

    // Name validation (2-50 letters and spaces)
    public static boolean isValidName(String name) {
        return name != null && name.matches("^[a-zA-Z ]{2,50}$");
    }

    // Email validation (standard format)
    public static boolean isValidEmail(String email) {
        String regex = "^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$";
        return email != null && Pattern.compile(regex).matcher(email).matches();
    }

    // Phone number validation (strict 10 digits)
    public static boolean isValidMobileNumber(String number) {
        if (number == null) return false;

        // Remove all formatting characters
        String cleanNumber = number.replaceAll("[^0-9+]", "");

        // Must be exactly 10 digits (with optional + prefix for international)
        if (cleanNumber.startsWith("+")) {
            // International: + followed by exactly 10 digits
            return cleanNumber.length() == 11 && cleanNumber.substring(1).matches("\\d{10}");
        } else {
            // Domestic: exactly 10 digits
            return cleanNumber.length() == 10 && cleanNumber.matches("\\d{10}");
        }
    }

    // Format phone number consistently
    public static String formatPhoneNumber(String number) {
        if (number == null) return null;

        // Preserve international prefix
        boolean isInternational = number.startsWith("+");
        String digits = number.replaceAll("[^0-9]", "");

        // Return in consistent format
        return isInternational ? "+" + digits : digits;
    }

    // Handle invalid input with retry option
    public static boolean handleInvalidInput(String errorMessage) {
        System.out.println("\n" + errorMessage);
        System.out.println("1. Exit this operation");
        System.out.println("2. Try again");
        System.out.print("Enter choice (1-2): ");

        while (true) {
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                if (choice == 1) return false; // Exit
                if (choice == 2) return true;  // Retry
                System.out.print("Please enter 1 or 2: ");
            } catch (InputMismatchException e) {
                System.out.print("Please enter a number (1 or 2): ");
                scanner.nextLine();
            }
        }
    }

    // Validate and format phone number with examples
    public static String validatePhoneNumberWithExamples(String input) {
        if (input == null) return null;

        String formatted = formatPhoneNumber(input);
        if (isValidMobileNumber(input)) {
            return formatted;
        }

        System.out.println("\nInvalid phone number format!");


        return null;
    }
}